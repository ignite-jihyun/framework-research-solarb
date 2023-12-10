package com.ignite.entity

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.assertEquals

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest(
    @Autowired val repository: CustomerRepository
) {

    @Test
    fun findAll() {
        repository.deleteAll()
        repository.save(Customer("Jack", "foo"))
        val customers = repository.findAll()
        assertEquals(1, customers.toList().size)
    }
}
