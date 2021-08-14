import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

//import net.minecraftforge.gradle.userdev.tasks.RenameJarInPlace
//import net.minecraftforge.gradle.mcp.task.GenerateSRG

val modGroup: String by extra
val modVersion: String by extra
val modBaseName: String by extra
val forgeVersion: String by extra
val mappingVersion: String by extra

buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://maven.minecraftforge.net/")
    }
    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:5.+") {
            isChanging = true
        }
    }
}

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    java
}

/*apply {
    plugin("net.minecraftforge.gradle")
}*/

version = modVersion
group = modGroup

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

/*configure<UserDevExtension> {
    mappings("snapshot",  "20210309-1.16.5")
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs {
        create("server") {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
            property("forge.logging.console.level", "debug")
        }
    }
}*/

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    //"minecraft"("net.minecraftforge:forge:$forgeVersion")
    api(kotlin("stdlib"))
    api(project(":core"))
}

/*val Project.minecraft: UserDevExtension
    get() = extensions.getByName<UserDevExtension>("minecraft")

tasks.withType<Jar> {
    inputs.property("version", project.version)

    baseName = modBaseName

    filesMatching("/mcmod.info") {
        expand(mapOf(
            "version" to project.version,
            "mcversion" to "1.16.4"
        ))
    }
}

tasks.create("copyResourceToClasses", Copy::class) {
    tasks.classes.get().dependsOn(this)
    dependsOn(tasks.processResources.get())
    onlyIf { gradle.taskGraph.hasTask(tasks.getByName("prepareRuns")) }
    into("$buildDir/classes/kotlin/main")
    from(tasks.processResources.get().destinationDir)
}

/*configurations {
    create("bin") {
        extendsFrom(configurations["compileClasspath"])
        exclude("net.minecraftforge", "forge")
    }
}

tasks.withType<ShadowJar> {
    configurations = listOf(project.configurations["bin"])
}

afterEvaluate {
    val reobf = extensions.getByName<NamedDomainObjectContainer<RenameJarInPlace>>("reobf")
    reobf.maybeCreate("shadowJar").run {
        mappings = tasks.getByName<GenerateSRG>("createMcpToSrg").output
    }
}*/