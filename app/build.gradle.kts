import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android.gradle.plugin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.safe.args)
}
apply(from = "../build-tasks.gradle.kts")
apply(plugin = "org.jetbrains.kotlin.android")

android {
    namespace = libs.versions.applicationId.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()

        val major = libs.versions.major.get().toInt()
        val minor = libs.versions.minor.get().toInt()
        val subMinor = libs.versions.subMinor.get().toInt()
        val buildNumber = libs.versions.buildNumber.get().toInt()
        versionName = "$major.$minor.$subMinor ($buildNumber)"

        archivesName.set("${rootProject.name}-$versionName")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kapt {
    correctErrorTypes = true

    javacOptions {
        // Increase the max count of errors from annotation processors.
        // Default is 100.
        option("-Xmaxerrs", 500)
    }
}

dependencies {
    implementation(project(path = ":domain"))
    implementation(project(path = ":data"))
    implementation(project(path = ":presentation"))

    // serialization
    implementation(libs.kotlin.serilization.json)

    // hilt
    implementation(libs.hilt.android)
    testImplementation(libs.testng)
    kapt(libs.hilt.android.compiler)

    // room
    implementation(libs.room.runtime)

    // retrofit
    implementation(libs.retrofit)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
}