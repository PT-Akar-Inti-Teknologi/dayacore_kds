rootProject.name = "KDS-DayaCore"
include(":composeCore:core")
include(":composeCore:libs:keyboardLib")
include(":composeApp")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("composeCore/libs.versions.toml"))
        }
    }
    repositories {
        google()
        mavenCentral()
    }
}
