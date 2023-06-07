plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
    application
}
repositories {
//    maven { setUrl("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
    mavenCentral()
}
dependencies {

    implementation(libs.jimmer.kotlin)
    implementation(libs.jimmer.sql)
    ksp(libs.jimmer.ksp)
    ksp(project(":core"))
    testImplementation(kotlin("test"))
}
ksp{
    /**
     * 此参数仅用于校验KSP插件功能正常与否，无实际意义
     */
    arg("helloJimmerHelper","Tom")
}
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}
tasks.test {
    useJUnitPlatform()
}
