package com.ignite.solarb.graphqlspqr

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.ignite.research"])
@EntityScan(basePackages = ["com.ignite.research"])
class GraphqlSpqrApplication

fun main(args: Array<String>) {
    runApplication<GraphqlSpqrApplication>(*args)
}
