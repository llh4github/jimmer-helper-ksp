plugins {
    alias(libs.plugins.kotlin)
    `java-library`
}
repositories {
//    maven { setUrl("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
    mavenCentral()
}
dependencies {
    api(libs.kotlinpoet)
    api(libs.kotlinpoet.ksp)

    api(libs.ktfmt)
    implementation(libs.ktlint.rule.engine.core)
    implementation(libs.ktlint.rule.engine)
    implementation(libs.googleKsp)
    implementation(libs.jimmer.sql)
    implementation(libs.jimmer.kotlin)
    implementation(libs.jimmer.ksp)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
