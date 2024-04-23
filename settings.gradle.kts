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
    repositories {
        google()
        mavenCentral()
    }
}
