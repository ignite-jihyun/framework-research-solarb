/*
 * Copyright 2021 Expedia, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ignite.graphql.examples.server.spring.query

import com.ignite.graphql.examples.server.spring.DATA_JSON_PATH
import com.ignite.graphql.examples.server.spring.GRAPHQL_ENDPOINT
import com.ignite.graphql.examples.server.spring.GRAPHQL_MEDIA_TYPE
import com.ignite.graphql.examples.server.spring.verifyOnlyDataExists
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
@TestInstance(PER_CLASS)
class ContextualQueryIT(@Autowired private val testClient: WebTestClient) {

    @Test
    fun `verify contextualQuery query`() {
        val query = "contextualQuery"

        testClient.post()
            .uri(GRAPHQL_ENDPOINT)
            .accept(APPLICATION_JSON)
            .contentType(GRAPHQL_MEDIA_TYPE)
            .bodyValue("query { $query(value: 1) { contextValue, passedInValue } }")
            .exchange()
            .expectStatus().isOk
            .verifyOnlyDataExists(query)
            .jsonPath("$DATA_JSON_PATH.$query.contextValue").isEqualTo("defaultContext")
            .jsonPath("$DATA_JSON_PATH.$query.passedInValue").isEqualTo("1")
    }

    @Test
    fun `verify contextualQuery query with context value`() {
        val query = "contextualQuery"

        testClient.post()
            .uri(GRAPHQL_ENDPOINT)
            .header("MyHeader", "hello")
            .accept(APPLICATION_JSON)
            .contentType(GRAPHQL_MEDIA_TYPE)
            .bodyValue("query { $query(value: 1) { contextValue, passedInValue } }")
            .exchange()
            .expectStatus().isOk
            .verifyOnlyDataExists(query)
            .jsonPath("$DATA_JSON_PATH.$query.contextValue").isEqualTo("hello")
            .jsonPath("$DATA_JSON_PATH.$query.passedInValue").isEqualTo("1")
    }
}
