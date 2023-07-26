/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    id("io.github.llh4github.kotlin-library-conventions")
    id("io.github.llh4github.publish")
    id("com.google.devtools.ksp") version "1.8.20-1.0.11"
}
dependencies {
    val jimmerVersion = property("version.jimmer")

    api(project(":core"))
    implementation("com.google.devtools.ksp:symbol-processing-api")
    ksp("org.babyfish.jimmer:jimmer-ksp:${jimmerVersion}")
    implementation("org.babyfish.jimmer:jimmer-ksp")
    implementation("org.babyfish.jimmer:jimmer-sql-kotlin")
    implementation("org.babyfish.jimmer:jimmer-core-kotlin")
    implementation("com.facebook:ktfmt")
}
