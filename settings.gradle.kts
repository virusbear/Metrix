pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

rootProject.name = "Metrix"
include("metrix-api")
include("metrix-micrometer")
