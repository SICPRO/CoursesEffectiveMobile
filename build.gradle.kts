plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false

    id("org.jetbrains.kotlin.android") version "2.3.10" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.9.7" apply false
}