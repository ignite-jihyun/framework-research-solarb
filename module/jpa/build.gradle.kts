import org.springframework.boot.gradle.tasks.bundling.BootJar

description = "jpa module"

plugins {
    id("com.ignite.graphql.examples.conventions")
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    `java-library`
}

kotlin {
    jvmToolchain(17)
}

tasks.named<BootJar>("bootJar") {
    isEnabled = false
}

dependencies {
    api(libs.spring.boot.starter.data.jpa)
    runtimeOnly(libs.mariadb.client)

    testImplementation(libs.h2database)
    testImplementation(libs.spring.boot.test)
}

tasks.test {
    systemProperty("spring.profiles.active", "test")
}
