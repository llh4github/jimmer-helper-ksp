val projectVersion = file("project.version").readText().trim()
allprojects {
    group = "com.github.llh4github"
    version = projectVersion

//    java.sourceCompatibility = JavaVersion.VERSION_11
//    java.targetCompatibility = JavaVersion.VERSION_11

}
repositories {
//    maven { setUrl("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
    mavenCentral()
}