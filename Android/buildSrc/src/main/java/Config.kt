object Config {

    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"

    object Dependencies {
        const val androidPlugin = "com.android.tools.build:gradle:${Versions.gradle}"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val daggerHiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.daggerHiltVersion}"
    }

    object Plugins {
        const val androidPlugin = "com.android.application"
        const val kotlinPlugin = "kotlin-android"
        const val kotlinKaptPlugin = "kotlin-kapt"
        const val daggerHiltPlugin = "dagger.hilt.android.plugin"
    }
}