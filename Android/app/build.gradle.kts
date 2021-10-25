plugins {
    id(Config.Plugins.androidPlugin)
    id(Config.Plugins.kotlinPlugin)
    id(Config.Plugins.kotlinExtensionsPlugin)
    id(Config.Plugins.kotlinKaptPlugin)
    id(Config.Plugins.daggerHiltPlugin)
}

android {
    compileSdk = Versions.compileSdkVersion

    defaultConfig {
        applicationId = ApplicationId.appId
        minSdk = Versions.minSdkVersion
        targetSdk = Versions.targetSdkVersion
        versionCode = Releases.versionCode
        versionName = Releases.versionName
        testInstrumentationRunner = Config.testRunner
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation (fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation (Libraries.kotlinStdLib)
    implementation (Libraries.appCompat)
    implementation (Libraries.ktsCore)
    implementation (Libraries.recyclerView)
    implementation (Libraries.cardView)
    implementation (Libraries.material)
    implementation (Libraries.circleImageView)

    implementation (Libraries.retrofit)
    implementation (Libraries.retrofitGsonConverter)
    implementation (Libraries.retrofitScalarsConverter)
    implementation (Libraries.picasso)

    implementation (Libraries.coroutinesCore)
    implementation (Libraries.coroutinesAndroid)

    implementation (Libraries.paging)

    implementation (Libraries.lifecycleExtension)
    implementation (Libraries.lifecycleViewModel)
    implementation (Libraries.fragmentExtension)

    implementation(Libraries.daggerHitl)
    kapt(Libraries.daggerHiltCompiler)

    implementation (Libraries.dagger)
    kapt (Libraries.daggerCompiler)
    implementation (Libraries.daggerAndroid)
    implementation (Libraries.daggerAndroidSupport)
    kapt (Libraries.daggerAndroidProcessor)

    implementation (Libraries.timber)
    debugImplementation(Libraries.leakCanary)

    testImplementation (Libraries.junit)
    androidTestImplementation (Libraries.junitExt)
    androidTestImplementation (Libraries.espressoCore)
}
