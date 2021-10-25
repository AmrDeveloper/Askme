object Config {

    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"

    object Dependencies {
        const val androidPlugin = "com.android.tools.build:gradle:${Versions.gradle}"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    }

    object Plugins {
        const val androidPlugin = "com.android.application"
        const val kotlinPlugin = "kotlin-android"
        const val kotlinExtensionsPlugin = "kotlin-android-extensions"
        const val kotlinKaptPlugin = "kotlin-kapt"
    }
}