import org.gradle.api.GradleException
import org.gradle.kotlin.dsl.register
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

tasks.register<Exec>("checkForUncommittedChanges") {
    commandLine("git", "diff", "--quiet", "--exit-code")
    isIgnoreExitValue = true
    doLast {
        if (executionResult.get().exitValue != 0) {
            throw GradleException("There are uncommitted changes. Please commit or stash them before running the buildAndSendApkToTelegram task.")
        }
    }
}

tasks.register("checkForUnpushedChanges") {
    doLast {
        val output = ByteArrayOutputStream()
        project.exec {
            commandLine("git", "cherry", "-v")
            standardOutput = output
            isIgnoreExitValue = true
        }
        if (output.toString().isNotBlank()) {
            throw GradleException("There are unpushed changes. Please push them before running the buildAndSendApkToTelegram task.")
        }
    }
}

tasks.register("incrementBuildNumber") {
    dependsOn("checkForUncommittedChanges", "checkForUnpushedChanges")

    doLast {
        // Read the current buildNumber from libs.versions.toml
        val versionsFile = project.file("../gradle/libs.versions.toml")
        val command = "grep buildNumber ${versionsFile.absolutePath} | cut -d '=' -f 2 | tr -d ' ' | tr -d '\"'"
        val output = ByteArrayOutputStream()
        project.exec {
            commandLine("sh", "-c", command)
            standardOutput = output
        }
        val buildNumber = output.toString().trim().toInt()

        // Increment the buildNumber by one
        val newBuildNumber = buildNumber + 1

        // Replace the buildNumber in libs.versions.toml with the new value
        project.exec {
            commandLine(
                "sed",
                "-i",
                "",
                "s/buildNumber = .*/buildNumber = \\\"$newBuildNumber\\\"/",
                versionsFile.absolutePath
            )
        }

        // Commit and push the changes
        project.exec {
            commandLine("git", "add", versionsFile.absolutePath)
        }
        project.exec {
            commandLine("git", "commit", "-m", "Increment build number to $newBuildNumber")
        }
        project.exec {
            commandLine("git", "push", "origin")
        }
    }
}

extra["sendApkToTelegram"] = fun(apk: File, message: String) {
    val boundary = "*****"
    val lineEnd = "\r\n"
    val twoHyphens = "--"

    val botToken = "5769836331:AAH0bySPAfmC8PYLsoGE67TcET_FkW6ckaY"
    val chatId = "-1796402825"

    val url = URL("https://api.telegram.org/bot$botToken/sendDocument")

    val connection = url.openConnection() as HttpURLConnection
    connection.doInput = true
    connection.doOutput = true
    connection.useCaches = false
    connection.requestMethod = "POST"
    connection.setRequestProperty("Connection", "Keep-Alive")
    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")

    DataOutputStream(connection.outputStream).use { outputStream ->
        // Send chat_id parameter
        outputStream.writeBytes(twoHyphens + boundary + lineEnd)
        outputStream.writeBytes("Content-Disposition: form-data; name=\"chat_id\"$lineEnd")
        outputStream.writeBytes(lineEnd)
        outputStream.writeBytes(chatId + lineEnd)

        // Send caption parameter
        outputStream.writeBytes(twoHyphens + boundary + lineEnd)
        outputStream.writeBytes("Content-Disposition: form-data; name=\"caption\"$lineEnd")
        outputStream.writeBytes(lineEnd)
        outputStream.writeBytes(message + lineEnd)

        // Send document parameter
        outputStream.writeBytes(twoHyphens + boundary + lineEnd)
        outputStream.writeBytes("Content-Disposition: form-data; name=\"document\";filename=\"${apk.name}\"$lineEnd")
        outputStream.writeBytes(lineEnd)
        apk.inputStream().use { inputStream ->
            inputStream.copyTo(outputStream)
        }
        outputStream.writeBytes(lineEnd)

        // End multipart/form-data
        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)
    }

    if (connection.responseCode != HttpURLConnection.HTTP_OK) {
        throw GradleException("Failed to send APK to Telegram channel: ${connection.responseMessage}")
    }
}