description = "jpa module"

plugins {
    id("com.ignite.graphql.examples.conventions")
    alias(libs.plugins.kotlin.spring)
    `java-library`
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    api(libs.spring.boot.starter.data.jpa)
    runtimeOnly(libs.mariadb.client)

    testImplementation(libs.kotlin.junit.test)
    testImplementation(libs.h2database)
    testImplementation(libs.spring.boot.test)
}

tasks.jar {
    enabled = true
}

tasks.test {
    systemProperty("spring.profiles.active", "test")
}
