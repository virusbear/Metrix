import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://maven.fabricmc.net")
        gradlePluginPortal()
    }
    dependencies {
        classpath("fabric-loom:fabric-loom.gradle.plugin:0.9-SNAPSHOT")
    }
}

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

apply {
    plugin("fabric-loom")
}

val minecraft_version: String by project
val yarn_mappings: String by project
val loader_version: String by project
val fabric_version: String by project

val bundle by configurations.creating

dependencies {
    "shadow"(kotlin("stdlib"))
    api(project(":core"))
    bundle(project(":core"))
    "shadow"(project(":fabric-api"))

    "minecraft"("com.mojang:minecraft:$minecraft_version")
    "mappings"("net.fabricmc:yarn:$yarn_mappings:v2")

    "modImplementation"("net.fabricmc:fabric-loader:$loader_version")
    "modImplementation"("net.fabricmc.fabric-api:fabric-api:$fabric_version")
}

val jar: Jar by tasks
val shadowJar: ShadowJar by tasks
val remapJar: RemapJarTask by tasks

jar.apply {
    archiveFileName.set("metrix-fabric-${project.version}.jar")
}

shadowJar.apply {
    archiveFileName.set("metrix-fabric-${project.version}.jar")
    configurations = listOf(bundle)
}

remapJar.apply {
    dependsOn(shadowJar)
    input.set(shadowJar.outputs.files.first())
}