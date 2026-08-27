plugins {
    kotlin("jvm") version "2.4.10"
    application
    id("io.github.cdsap.fatbinary") version "1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


fatBinary {
    mainClass = "io.github.cdsap.projectgraphmetrics.cli.Main"
    name = "projectGraphMetrics"
}

dependencies {
    implementation(project(":projectgraphmetrics"))
    implementation("com.jakewharton.picnic:picnic:0.7.0")
    implementation("com.github.ajalt.clikt:clikt:5.1.0")
    testImplementation("junit:junit:4.13.2")
    implementation(kotlin("stdlib-jdk8"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("io.github.cdsap.projectgraphmetrics.cli.MainKt")
}
