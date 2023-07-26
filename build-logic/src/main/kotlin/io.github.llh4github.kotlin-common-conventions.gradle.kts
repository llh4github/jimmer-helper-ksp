/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    gradlePluginPortal()
}

group = rootProject.group
version=rootProject.version

dependencies {

    val jimmerVersion = property("version.jimmer")
    constraints {
        implementation("com.google.devtools.ksp:symbol-processing-api:1.8.20-1.0.11")
        implementation("org.babyfish.jimmer:jimmer-ksp:$jimmerVersion")
        implementation("org.babyfish.jimmer:jimmer-sql-kotlin:${jimmerVersion}")
        implementation("org.babyfish.jimmer:jimmer-core-kotlin:${jimmerVersion}")
        implementation("com.facebook:ktfmt:0.44")
    }
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.9.2")
        }
    }
}
