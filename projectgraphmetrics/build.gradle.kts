import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.3.0"
    `maven-publish`
    `signing`
}

group = "io.github.cdsap"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jgrapht:jgrapht-core:1.5.2")
    implementation("org.jgrapht:jgrapht-io:1.5.2")
    implementation("org.nield:kotlin-statistics:1.2.1")
    testImplementation("junit:junit:4.13.2")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}

configure<JavaPluginExtension> {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            name = "Snapshots"
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")

            credentials {
                username = System.getenv("USERNAME_SNAPSHOT")
                password = System.getenv("PASSWORD_SNAPSHOT")
            }
        }
        maven {
            name = "Release"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")

            credentials {
                username = System.getenv("USERNAME_SNAPSHOT")
                password = System.getenv("PASSWORD_SNAPSHOT")
            }
        }
    }
    publications {
        create<MavenPublication>("libPublication") {
            from(components["java"])
            artifactId = "projectgraphmetrics"
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                scm {
                    connection.set("scm:git:git://github.com/cdsap/GEApiData/")
                    url.set("https://github.com/cdsap/ProjectGraphMetrics/")
                }
                name.set("projectgraphmetrics")
                url.set("https://github.com/cdsap/ProjectGraphMetrics/")
                description.set(
                    "Returns graph metrics by project"
                )
                licenses {
                    license {
                        name.set("The MIT License (MIT)")
                        url.set("https://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("cdsap")
                        name.set("Inaki Villar")
                    }
                }
            }
        }
    }
}

if (extra.has("signing.keyId")) {
    afterEvaluate {
        configure<SigningExtension> {
            (
                extensions.getByName("publishing") as
                    PublishingExtension
                ).publications.forEach {
                    sign(it)
                }
        }
    }
}
