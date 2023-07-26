/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    id("io.github.llh4github.kotlin-application-conventions")
    id("com.google.devtools.ksp") version "1.8.20-1.0.11"
}

dependencies {
    implementation(project(":jimmer-helper-core"))
    ksp(project(":jimmer-helper-ksp"))

    val jimmerVersion = property("version.jimmer")

    ksp("org.babyfish.jimmer:jimmer-ksp:${jimmerVersion}")
    implementation("org.babyfish.jimmer:jimmer-sql-kotlin")
    implementation("org.babyfish.jimmer:jimmer-core-kotlin")
}

application {
    // Define the main class for the application.
    mainClass.set("io.github.llh4github.example.AppKt")
}
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}