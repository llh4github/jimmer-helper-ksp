plugins {
    alias(libs.plugins.kotlin)
    `java-library`
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
