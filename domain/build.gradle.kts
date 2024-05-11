plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    // inject annotations
    implementation(libs.javax.inject)

    // coroutines
    implementation(libs.coroutines.core)
}