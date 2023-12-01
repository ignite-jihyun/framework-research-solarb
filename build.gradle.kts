allprojects {
    buildscript {
        repositories {
            mavenCentral()
            gradlePluginPortal()
        }
    }

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    apply(plugin = "jacoco")

    tasks.withType<Test>() {
        finalizedBy("jacocoTestReport")
    }

    tasks.withType<JacocoReport> {
        reports {
            xml.required.set(true)
        }
    }
}

plugins {
    id("org.sonarqube") version "4.4.1.3373"
}

sonar {
    properties {
        property("sonar.projectKey", "ignite-jihyun_framework-research-solarb")
        property("sonar.organization", "ignite-jihyun")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
