plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.20"
    id("com.github.johnrengelman.shadow")
}

val bundle by configurations.creating

dependencies {
    api(kotlin("stdlib"))
    bundle(kotlin("stdlib"))
    api(project(":api"))
    bundle(project(":api"))

    "shadow"("org.apache.logging.log4j:log4j-api:2.11.2")
    api("io.ktor:ktor-server-netty:1.6.1")
    bundle("io.ktor:ktor-server-netty:1.6.1")
    api("io.ktor:ktor-server-core:1.6.1")
    bundle("io.ktor:ktor-server-core:1.6.1")
    api("io.ktor:ktor-metrics-micrometer:1.6.1")
    bundle("io.ktor:ktor-metrics-micrometer:1.6.1")
    api("io.micrometer:micrometer-registry-prometheus:1.7.2")
    bundle("io.micrometer:micrometer-registry-prometheus:1.7.2")

    api("org.jetbrains.kotlinx:kotlinx-serialization-properties:1.2.2")
    bundle("org.jetbrains.kotlinx:kotlinx-serialization-properties:1.2.2")
}

//TODO: replace with maven publication
tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    configurations = listOf(bundle)
    archiveBaseName.set("metrix-core")
}