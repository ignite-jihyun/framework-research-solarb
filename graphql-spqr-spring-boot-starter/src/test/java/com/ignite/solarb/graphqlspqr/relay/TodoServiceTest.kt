package com.ignite.solarb.graphqlspqr.relay

import com.ignite.solarb.graphqlspqr.AbstractGraphqlSpqrTest
import com.ignite.solarb.graphqlspqr.GraphqlSpqrApplication
import org.json.JSONObject
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
class TodoServiceTest(
    @Autowired
    private val webTestClient: WebTestClient,
) : AbstractGraphqlSpqrTest(webTestClient) {
    @Test
    @Order(1)
    fun `user todos query`() {
        val userIdPlaceholder = "\$userId"
        val query =
            """
            query TodoAppQuery(
              $userIdPlaceholder: String
            ) {
              user(id: $userIdPlaceholder) {
                ...TodoList_user
              }
            }

            fragment AddTodoMutation_user on User {
              userId
              id
              totalCount
            }

            fragment ChangeTodoStatusMutation_todo on Todo {
              id
            }

            fragment ChangeTodoStatusMutation_user on User {
              id
              userId
              completedCount
            }

            fragment MarkAllTodosMutation_todoEdge on TodoEdge {
              node {
                id
              }
            }

            fragment MarkAllTodosMutation_user on User {
              id
              userId
              totalCount
            }

            fragment RemoveCompletedTodosMutation_todoConnection on TodoConnection {
              edges {
                node {
                  id
                  complete
                }
              }
            }

            fragment RemoveCompletedTodosMutation_user on User {
              id
              userId
              totalCount
            }

            fragment RemoveTodoMutation_todo on Todo {
              id
              complete
            }

            fragment RemoveTodoMutation_user on User {
              id
              userId
              totalCount
              completedCount
            }

            fragment RenameTodoMutation_todo on Todo {
              id
              text
            }

            fragment TodoListFooter_todoConnection on TodoConnection {
              ...RemoveCompletedTodosMutation_todoConnection
            }

            fragment TodoListFooter_user on User {
              totalCount
              completedCount
              ...RemoveCompletedTodosMutation_user
            }

            fragment TodoList_user on User {
              todos(first: 2147483647) {
                edges {
                  node {
                    id
                    ...Todo_todo
                    __typename
                  }
                  ...MarkAllTodosMutation_todoEdge
                  cursor
                }
                ...TodoListFooter_todoConnection
                pageInfo {
                  endCursor
                  hasNextPage
                }
              }
              totalCount
              completedCount
              ...AddTodoMutation_user
              ...MarkAllTodosMutation_user
              ...Todo_user
              ...TodoListFooter_user
            }

            fragment Todo_todo on Todo {
              complete
              text
              ...ChangeTodoStatusMutation_todo
              ...RenameTodoMutation_todo
              ...RemoveTodoMutation_todo
            }

            fragment Todo_user on User {
              ...ChangeTodoStatusMutation_user
              ...RemoveTodoMutation_user
            }
            """
        expectQueryResult(
            query,
            JSONObject().put("userId", "me"),
            """ {"data":{"user":{"todos":{"edges":[{"node":{"id":"1"}},{"node":{"id":"2"}},{"node":{"id":"3"}}],"pageInfo":{"hasNextPage":false}},"totalCount":3,"userId":"me","id":"me"}}} """,
        )
    }

    @Test
    @Order(2)
    fun renameTodo() {
        val inputPlaceholder = "\$input"
        val query =
            """
            mutation RenameTodoMutation(
              $inputPlaceholder: RenameTodoInput!
            ) {
              renameTodo(input: $inputPlaceholder) {
                todo {
                  id
                  text
                }
              }
            }
            """
        expectQueryResult(
            query,
            JSONObject().put("input", JSONObject().put("id", "1").put("text", "first todo renamed")),
            """ {"data":{"renameTodo":{"todo":{"id":"1","text":"first todo renamed"}}}} """,
        )
    }

    @Test
    @Order(2)
    fun changeTodoStatus() {
        val inputPlaceholder = "\$input"
        val query =
            """
            mutation ChangeTodoStatusMutation(
              $inputPlaceholder: ChangeTodoStatusInput!
            ) {
              changeTodoStatus(input: $inputPlaceholder) {
                todo {
                  id
                  complete
                }
                user {
                  id
                  userId
                  completedCount
                }
              }
            }
            """
        expectQueryResult(
            query,
            JSONObject().put("input", JSONObject().put("userId", "me").put("id", "1").put("complete", true)),
            """ {"data":{"changeTodoStatus":{"todo":{"id":"1","complete":true},"user":{"id":"me","userId":"me","completedCount":1}}}} """,
        )

        expectQueryResult(
            query,
            JSONObject().put("input", JSONObject().put("userId", "me").put("id", "1").put("complete", false)),
            """ {"data":{"changeTodoStatus":{"todo":{"id":"1","complete":false},"user":{"id":"me","userId":"me","completedCount":0}}}} """,
        )
    }

    @Test
    @Order(3)
    fun `addTodo removeTodo`() {
        val inputPlaceholder = "\$input"
        val addQuery =
            """
            mutation AddTodoMutation(
              $inputPlaceholder: AddTodoInput!
            ) {
              addTodo(input: $inputPlaceholder) {
                todoEdge {
                  node {
                    id
                    complete
                    text
                  }
                  cursor
                }
                user {
                  id
                  userId
                  totalCount
                  completedCount
                }
              }
            }
            """
        expectQueryResult(
            addQuery,
            JSONObject().put("input", JSONObject().put("userId", "me").put("text", "third todo")),
            """ {"data":{"addTodo":{"todoEdge":{"node":{"id":"4","complete":false,"text":"third todo"}},"user":{"id":"me","userId":"me","totalCount":4,"completedCount":0}}}} """,
        )

        val removeQuery =
            """
            mutation RemoveTodoMutation(
              $inputPlaceholder: RemoveTodoInput!
            ) {
              removeTodo(input: $inputPlaceholder) {
                deletedTodoId
                user {
                  completedCount
                  totalCount
                }
              }
            }
            """
        expectQueryResult(
            removeQuery,
            JSONObject().put("input", JSONObject().put("userId", "me").put("id", "4")),
            """ {"data":{"removeTodo":{"deletedTodoId":"{\"id\":\"4\"}","user":{"completedCount":0,"totalCount":3}}}} """,
        )
    }

    @Test
    @Order(4)
    fun `markAllTodos removeCompletedTodos`() {
        val inputPlaceholder = "\$input"
        val markAllQuery =
            """
            mutation MarkAllTodosMutation(
                $inputPlaceholder: MarkAllTodosInput!
            ) {
                markAllTodos(input: $inputPlaceholder) {
                    changedTodos {
                        id
                        complete
                    }
                    user {
                        id
                        completedCount
                    }
                }
            }
            """
        expectQueryResult(
            markAllQuery,
            JSONObject().put("input", JSONObject().put("userId", "me").put("complete", true)),
            """ {"data":{"markAllTodos":{"changedTodos":[{"id":"1","complete":true},{"id":"2","complete":true},{"id":"3","complete":true}],"user":{"id":"me","completedCount":3}}}} """,
        )

        expectQueryResult(
            markAllQuery,
            JSONObject().put("input", JSONObject().put("userId", "me").put("complete", false)),
            """ {"data":{"markAllTodos":{"changedTodos":[{"id":"1","complete":false},{"id":"2","complete":false},{"id":"3","complete":false}],"user":{"id":"me","completedCount":0}}}} """,
        )

        val removeCompletedQuery =
            """
            mutation RemoveCompletedTodosMutation(
              $inputPlaceholder: RemoveCompletedTodosInput!
            ) {
              removeCompletedTodos(input: $inputPlaceholder) {
                deletedTodoIds
                user {
                  id
                  userId
                  totalCount
                  completedCount
                }
              }
            }
            """
        expectQueryResult(
            removeCompletedQuery,
            JSONObject().put("input", JSONObject().put("userId", "me")),
            """ {"data":{"removeCompletedTodos":{"deletedTodoIds":[],"user":{"id":"me","userId":"me","totalCount":3,"completedCount":0}}}} """,
        )

        expectQueryResult(
            markAllQuery,
            JSONObject().put("input", JSONObject().put("userId", "me").put("complete", true)),
            """ {"data":{"markAllTodos":{"user":{"completedCount":3}}}} """,
        )

        expectQueryResult(
            removeCompletedQuery,
            JSONObject().put("input", JSONObject().put("userId", "me")),
            """ {"data":{"removeCompletedTodos":{"deletedTodoIds":["{\"id\":\"1\"}","{\"id\":\"2\"}","{\"id\":\"3\"}"],"user":{"id":"me","userId":"me","totalCount":0,"completedCount":0}}}} """,
        )
    }
}
