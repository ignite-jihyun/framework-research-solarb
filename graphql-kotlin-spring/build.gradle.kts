description = "An example GraphQL Spring server"

plugins {
    id("com.ignite.graphql.examples.conventions")
    alias(libs.plugins.graphql.kotlin)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.graphql.kotlin.spring.server)
    implementation(libs.graphql.kotlin.hooks.provider)
    implementation(libs.spring.boot.validation)
    implementation(libs.spring.boot.webflux)
    implementation(project(":module:entity"))
    runtimeOnly(libs.mariadb.client)

    testImplementation(libs.h2database)
    testImplementation(libs.spring.boot.test)
    testImplementation(libs.reactor.test)
}

graphql {
    schema {
        packages = listOf("com.ignite.graphql.examples.server.spring")
    }
}
