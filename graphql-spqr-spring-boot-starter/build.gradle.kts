description = "An example GraphQL SPQR Spring Boot Starter"

plugins {
    id("com.ignite.graphql.examples.conventions")
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
}

allOpen {
    annotations("jakarta.persistence.Entity", "jakarta.persistence.MappedSuperclass", "jakarta.persistence.Embeddable")
}

dependencies {
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.webflux)
    implementation(libs.graphql.spqr.spring.boot)
    implementation(project(":module:entity"))
    runtimeOnly(libs.mariadb.client)

    testImplementation(libs.h2database)
    testImplementation(libs.spring.boot.test)
}
