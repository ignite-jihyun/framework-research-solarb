package com.ignite.jpa.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile

@SpringBootApplication
@Profile("test")
class AccessingDataJpaApplication

fun main(args: Array<String>) {
    runApplication<AccessingDataJpaApplication>(*args)
}
