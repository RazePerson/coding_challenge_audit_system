plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")

    implementation("org.apache.commons:commons-lang3:3.13.0")

    testImplementation("org.testng:testng:7.8.0")
    testImplementation("org.assertj:assertj-core:3.24.2")


}

tasks.named<Test>("test") {
    useTestNG()
    testLogging {
        events("passed", "skipped", "failed")
    }
}