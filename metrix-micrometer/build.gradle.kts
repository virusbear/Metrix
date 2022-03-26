plugins {
    kotlin("jvm")
    `library-publishing`
}

dependencies {
    implementation(kotlin("stdlib"))

    api(project(":metrix-api"))
    api("io.micrometer:micrometer-core:1.8.4")
}