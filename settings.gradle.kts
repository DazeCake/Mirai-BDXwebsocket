pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.github.johnrengelman.shadow" -> useModule("com.github.jengelman.gradle.plugins:shadow:${requested.version}")
            }
        }
    }

    repositories {
        mavenLocal()
        jcenter()
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://kotlin.bintray.com/kotlinx")
    }
}

buildscript {

}

rootProject.name = "BDXWebSocket"

