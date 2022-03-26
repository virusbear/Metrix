plugins {
    kotlin("jvm")
    `library-publishing`
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.insert-koin:koin-core:3.2.0-beta-1")
}