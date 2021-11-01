import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31" apply false
    id("com.github.johnrengelman.shadow") version "7.1.0" apply false
}

subprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        jcenter()
        google()
    }

    group = "com.github.virusbear.metrix"
    version = "1.0"

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "16"
        }
    }
}