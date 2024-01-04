package com.ignite.solarb.graphqlspqr

import org.json.JSONException
import org.json.JSONObject
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import kotlin.test.Test

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [GraphqlSpqrApplication::class]
)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class GraphqlSpqrControllerTest(
    @Autowired
    private val webTestClient: WebTestClient
) {
    private val GRAPHQL_PATH = "/graphql"

    @Test
    @Order(1)
    fun board() {
        val query = """ {board(boardId: 2) { id title comments { id content } }} """
        val expectedJSON = """ {"data":{"board":{"id":2,"title":"board title 2","comments":[{"id":3},{"id":4}]}}} """
        expectQueryResult(query, expectedJSON)
    }


    @Test
    @Order(1)
    fun boards() {
        val query = """ {boards { id comments { id content }}} """
        val expectedJSON = """ {"data":{"boards":[{"id":1},{"id":2},{"id":3}]}} """
        expectQueryResult(query, expectedJSON)
    }

    @Test
    @Order(1)
    fun comments() {
        val query = """ {comments { id }} """
        val expectedJSON = """ {"data":{"comments":[{"id":1},{"id":2},{"id":3},{"id":4},{"id":5},{"id":6},{"id":7},{"id":8},{"id":9}]}} """
        expectQueryResult(query, expectedJSON)
    }

    @Test
    @Order(1)
    fun comment() {
        val query = """ {comment(commentId: 3) { id content board { id }}} """
        val expectedJSON = """ {"data":{"comment":{"id":3,"content":"comment content 3","board":{"id":2}}}} """
        expectQueryResult(query, expectedJSON)
    }

    @Test
    @Order(1)
    fun researchUsers() {
        val query = """ {researchUsers { id }} """
        val expectedJSON = """ {"data":{"researchUsers":[{"id":1},{"id":2}]}} """
        expectQueryResult(query, expectedJSON)
    }

    @Test
    @Order(1)
    fun researchUser() {
        val query = """ {researchUser(userId: 1) { id name}} """
        val expectedJSON = """ {"data":{"researchUser":{"id":1,"name":"name 1"}}} """
        expectQueryResult(query, expectedJSON)
    }

    @Test
    @Order(2)
    fun mutationBoard() {
        val query = """ mutation {board(board: {title: "title 4", content: "content: 4"})} """
        val expectedJSON = """ {"data":{"board":4}} """
        expectQueryResult(query, expectedJSON)
    }

    @Test
    @Order(2)
    fun mutationComment() {
        val query = """ mutation {comment(comment: {board: {id: 1}, content: "content: 10"})} """
        val expectedJSON = """ {"data":{"comment":10}} """
        expectQueryResult(query, expectedJSON)
    }

    @Test
    @Order(2)
    fun mutationUser() {
        val query = """ mutation {researchUser(user: {name: "name 3"})} """
        val expectedJSON = """ {"data":{"researchUser":3}} """
        expectQueryResult(query, expectedJSON)
    }

    @Test
    @Order(1)
    fun commentsByDataLoader() {
        expectQueryResult(
            """ {board(boardId: 2) { commentsByDataLoader { id }}} """,
            """ {"data":{"board":{"commentsByDataLoader":[{"id":3},{"id":4}]}}} """
        )
        expectQueryResult(
            """ {board(boardId: 2) { commentsByDataLoaderByBoardId { id }}} """,
            """ {"data":{"board":{"commentsByDataLoaderByBoardId":[{"id":3},{"id":4}]}}} """
        )
    }

    @Test
    @Order(1)
    fun lastThreeComments() {
        expectQueryResult(
            """ {board(boardId: 3) { lastThreeComments { id } }} """,
            """ {"data":{"board":{"lastThreeComments":[{"id":7},{"id":8},{"id":9}]}}} """
        )
        expectQueryResult(
            """ {board(boardId: 3) { lastThreeCommentsByBoardId { id } }} """,
            """ {"data":{"board":{"lastThreeCommentsByBoardId":[{"id":7},{"id":8},{"id":9}]}}} """
        )
    }

    private fun expectQueryResult(query: String, expectedJSON: String) {
        webTestClient.post()
            .uri(GRAPHQL_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(toJSON(query)), String::class.java)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(
                expectedJSON, false
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
