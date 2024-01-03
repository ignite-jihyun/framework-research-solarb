package com.ignite.solarb.graphqlspqr

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [GraphqlSpqrApplication::class],
)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class GraphqlSpqrServiceTest(
    @Autowired private val webTestClient: WebTestClient,
) : AbstractGraphqlSpqrTest(webTestClient) {
    @Test
    @Order(1)
    fun board2s() {
        val query = """ {board2s(board2: { id: "0" }) { id }} """
        val expectedJSON = """ {"data":{"board2s":[]}} """
        expectQueryResult(query, expectedJSON)
    }
}
