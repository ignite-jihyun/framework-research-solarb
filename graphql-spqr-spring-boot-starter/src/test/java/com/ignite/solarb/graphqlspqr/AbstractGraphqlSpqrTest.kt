package com.ignite.solarb.graphqlspqr

import org.json.JSONException
import org.json.JSONObject
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

abstract class AbstractGraphqlSpqrTest(
    private val webTestClient: WebTestClient,
) {
    private val GRAPHQL_PATH = "/graphql"


    fun expectQueryResult(query: String, expectedJSON: String) {
        webTestClient.post().uri(GRAPHQL_PATH).contentType(MediaType.APPLICATION_JSON).body(Mono.just(toJSON(query)), String::class.java).exchange().expectStatus().isOk().expectBody().json(
            expectedJSON,
            false,
        )
    }

    private fun toJSON(query: String): String {
        return try {
            JSONObject().put("query", query).toString()
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
    }
}
