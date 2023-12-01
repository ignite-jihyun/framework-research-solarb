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
