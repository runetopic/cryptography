plugins {
    kotlin("jvm")
    `maven-publish`
    signing
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.mockk:mockk:1.12.2")
}

group = "com.runetopic.cryptography"
version = "1.1.4-SNAPSHOT"

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_11.majorVersion))
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_11.majorVersion))
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                packaging = "jar"
                name.set("Runetopic Cryptography Library")
                description.set("Library to hold useful cryptographic algorithims.")
                url.set("https://github.com/runetopic/cryptography")

                developers {
                    developer {
                        id.set("tylert")
                        name.set("Tyler Telis")
                        email.set("xlitersps@gmail.com")
                    }

                    developer {
                        id.set("ultraviolet-jordan")
                        name.set("Jordan Abraham")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/runetopic/cryptography.git")
                    developerConnection.set("scm:git:ssh://github.com/runetopic/cryptography.git")
                    url.set("http://github.com/rune-topic/")
                }
            }

            artifact(tasks["javadocJar"])
            artifact(tasks["sourcesJar"])
        }
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
        }
    }
    repositories {
        val ossrhUsername: String by project
        val ossrhPassword: String by project

        maven {
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/releases/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
