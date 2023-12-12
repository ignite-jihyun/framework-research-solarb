package com.ignite.entity

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class CustomerCommandLineRunner(
    val repository: CustomerRepository,
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(CustomerCommandLineRunner::class.java)
    override fun run(vararg args: String?) {
        // save a few customers
        repository.save(Customer("Jack", "foo"))
        repository.save(Customer("Chloe", "O'Brian"))
        repository.save(Customer("Kim", "Bauer"))
        repository.save(Customer("David", "Palmer"))
        repository.save(Customer("Michelle", "bar"))

        // fetch all customers
        log.info("Customers found with findAll():")
        log.info("-------------------------------")
        repository.findAll().forEach(Consumer { customer -> log.info(customer.toString()) })
        log.info("")

        // fetch an individual customer by ID
        val customer = repository.findById(1L)
        log.info("Customer found with findById(1L):")
        log.info("--------------------------------")
        log.info(customer.toString())
        log.info("")

        // fetch customers by last name
        log.info("Customer found with findByLastName('Bauer'):")
        log.info("--------------------------------------------")
        repository.findByLastName("Bauer")?.forEach(Consumer { bauer -> log.info(bauer.toString()) })
        log.info("")
    }
}
