plugins {
    alias(libs.plugins.kotlin)
//    `java-library`
    `maven-publish`
    id("signing")
}
repositories {
//    maven { setUrl("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
    mavenCentral()
}
dependencies {
    implementation(project(":core"))

    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)

    implementation(libs.ktfmt)
    implementation(libs.googleKsp)
    implementation(libs.jimmer.sql)
    implementation(libs.jimmer.kotlin)
    implementation(libs.jimmer.ksp)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
val jarSources by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.map { it.allSource })
}

val jarJavadoc by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}
publishing {
    publications {
        create<MavenPublication>("dist") {
            from(components["java"])
            artifact(jarSources)
            artifact(jarJavadoc)

            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            pom {
                name.set("${project.group}:${project.name}")
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                scm {
                    url.set("https://github.com/llh4github/jimmer-helper-ksp")
                    connection.set("scm:git:https://github.com/kotlin-orm/ktorm.git")
                    developerConnection.set("scm:git:ssh://git@github.com:llh4github/jimmer-helper-ksp.git")
                }
            }
            repositories {
                maven {
                    name = "central"
                    url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
                    credentials {
                        username = System.getenv("OSSRH_USER")
                        password = System.getenv("OSSRH_PASSWORD")
                    }
                }
                maven {
                    name = "snapshot"
                    url = uri("https://oss.sonatype.org/content/repositories/snapshots")
                    credentials {
                        username = System.getenv("OSSRH_USER")
                        password = System.getenv("OSSRH_PASSWORD")
                    }
                }
            }

        }
    }
}
signing {
    val keyId = System.getenv("GPG_KEY_ID")
    val secretKey = System.getenv("GPG_SECRET_KEY")
    val password = System.getenv("GPG_PASSWORD")

    setRequired {
        !project.version.toString().endsWith("SNAPSHOT")
    }

    useInMemoryPgpKeys(keyId, secretKey, password)
    sign(publishing.publications["dist"])
}