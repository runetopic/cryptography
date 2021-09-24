plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
}

group = "com.runetopic.cryptography"
java.sourceCompatibility = JavaVersion.VERSION_16
java.targetCompatibility = JavaVersion.VERSION_16

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}