plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android.gradle.plugin)
    alias(libs.plugins.safe.args)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.codesample.presentation"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = libs.versions.testRunner.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(path = ":domain"))
    implementation(project(path = ":zoom_layout"))

    // android libs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // test libs
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    //phone number
    implementation(libs.phone.number)

    // model watcher
    implementation(libs.mvi.core.diff)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // seismic
    implementation(libs.seismic)

    // better link movement method
    implementation(libs.better.link.movement.method)

    // coil
    implementation(libs.coil)
}