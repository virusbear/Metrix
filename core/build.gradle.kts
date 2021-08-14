plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.20"
}

dependencies {
    implementation(kotlin("stdlib"))
    api(project(":api"))

    implementation("org.apache.logging.log4j:log4j-api:2.11.2")
    api("io.ktor:ktor-server-netty:1.6.1")
    api("io.ktor:ktor-server-core:1.6.1")
    api("io.ktor:ktor-metrics-micrometer:1.6.1")
    api("io.micrometer:micrometer-registry-prometheus:1.7.2")

    api("org.jetbrains.kotlinx:kotlinx-serialization-properties:1.2.2")
}