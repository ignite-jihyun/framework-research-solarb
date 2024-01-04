package com.ignite.solarb.graphqlspqr

import org.json.JSONObject
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

abstract class AbstractGraphqlSpqrTest(
    private val webTestClient: WebTestClient,
) {
    fun expectQueryResult(
        query: String,
        expectedJSON: String,
    ) {
        val json = Mono.just(toJSON(query))
        expectQueryResult(json, expectedJSON)
    }

    fun expectQueryResult(
        query: String,
        variables: JSONObject,
        expectedJSON: String,
    ) {
        val json = Mono.just(toJSON(query, variables))
        expectQueryResult(json, expectedJSON)
    }

    fun expectQueryResult(
        json: Mono<String>,
        expectedJSON: String,
    ) {
        webTestClient
            .post()
            .uri("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .body(json, String::class.java)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody().json(
                expectedJSON,
                false,
            )
    }

    private fun toJSON(query: String): String {
        return JSONObject().put("query", query).toString()
    }

    private fun toJSON(
        query: String,
        variables: JSONObject,
    ): String {
        return JSONObject().put("query", query).put("variables", variables).toString()
    }
}
