description = "jpa module"

plugins {
    id("com.ignite.graphql.examples.conventions")
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.h2database)
    implementation("org.mariadb.jdbc:mariadb-java-client:3.1.2")
    testImplementation(libs.spring.boot.test)
}

tasks.test {
    systemProperty("spring.profiles.active", "test")
}
