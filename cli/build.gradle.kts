import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.24"
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
    implementation("com.github.ajalt.clikt:clikt:3.5.4")
    testImplementation("junit:junit:4.13.2")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("io.github.cdsap.projectgraphmetrics.cli.MainKt")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
