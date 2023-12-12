description = "entity module"

plugins {
    id("com.ignite.graphql.examples.conventions")
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    `java-library`
}

tasks.jar {
    enabled = true
}

allOpen {
    annotations("jakarta.persistence.Entity", "jakarta.persistence.MappedSuperclass", "jakarta.persistence.Embeddable")
}

dependencies {
    api(project(":module:jpa"))

    testImplementation(libs.h2database)
    testImplementation(libs.spring.boot.test)
    testImplementation(libs.kotlin.junit.test)
}

tasks.test {
    systemProperty("spring.profiles.active", "test")
}
