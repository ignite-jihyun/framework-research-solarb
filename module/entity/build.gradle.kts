import org.springframework.boot.gradle.tasks.bundling.BootJar

description = "entity module"

plugins {
    id("com.ignite.graphql.examples.conventions")
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
//    alias(libs.plugins.kotlin.jpa)
//    alias(libs.plugins.kotlin.allopen)
}

tasks.named<BootJar>("bootJar") {
    isEnabled = false
}

allOpen {
    annotations("jakarta.persistence.Entity", "jakarta.persistence.MappedSuperclass", "jakarta.persistence.Embeddable")
}

dependencies {
    api(project(":module:jpa"))

    testImplementation(libs.h2database)
    testImplementation(libs.spring.boot.test)
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

tasks.test {
    systemProperty("spring.profiles.active", "test")
}
