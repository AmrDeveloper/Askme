buildscript {

    repositories {
        google()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath (Config.Dependencies.androidPlugin)
        classpath (Config.Dependencies.kotlinGradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}
