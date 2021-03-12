plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.31"
    id("com.gradle.plugin-publish") version "0.13.0"
    id("java-gradle-plugin")
}

group "io.testery"
project.version = "1.5"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.31")
    implementation("com.github.jengelman.gradle.plugins:shadow:6.1.0")
    implementation("com.squareup.okhttp3:okhttp:4.2.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.6.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

gradlePlugin {
    plugins {
        create("testery") {
            id = "io.testery"
            implementationClass = "io.testery.TesteryPlugin"
        }
    }
}

pluginBundle {
    website = "http://testery.io/"
    vcsUrl = "https://github.com/testery/gradle-plugin"
    tags = listOf("test")

    description = "Plugin for interacting with Testery from Gradle"

    (plugins) {
        "testery" {
            displayName = "Testery"
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}