package com.ignite.solarb.graphqlspqr.relay

import graphql.relay.DefaultConnection
import graphql.relay.DefaultConnectionCursor
import graphql.relay.DefaultEdge
import graphql.relay.DefaultPageInfo
import io.leangen.graphql.annotations.GraphQLId
import io.leangen.graphql.annotations.GraphQLQuery
import io.leangen.graphql.annotations.types.GraphQLInterface

data class User(
    @GraphQLId(relayId = true)
    override val id: String,
    val userId: String,
    val totalCount: Int = 0,
    val completedCount: Int = 0,
) : Node

class Todo(
    @GraphQLId(relayId = true)
    override val id: String,
    var text: String = "",
    var complete: Boolean = false,
) : Node

data class TodoConnection(
    // Accidental override: The following declarations have the same JVM signature (getEdges()Ljava/util/List;):
    @GraphQLQuery(name = "edges")
    val edges2: List<TodoEdge>,
    val pageInfo: CustomPageInfo,
) : DefaultConnection<Todo>(edges2, pageInfo)

data class CustomPageInfo(
    private val startCursor: DefaultConnectionCursor?,
    private val endCursor: DefaultConnectionCursor?,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean,
) : DefaultPageInfo(startCursor, endCursor, hasPreviousPage, hasNextPage) {
    @GraphQLQuery(name = "endCursor")
    fun getCustomEndCursor(): String {
        return endCursor?.value ?: ""
    }

    @GraphQLQuery(name = "startCursor")
    fun getCustomStartCursor(): String {
        return startCursor?.value ?: ""
    }
}

data class TodoEdge(
    val node: Todo,
    private val cursor: DefaultConnectionCursor,
) : DefaultEdge<Todo>(node, cursor) {
    @GraphQLQuery(name = "cursor")
    fun getCustomCursor(): String {
        return cursor.value
    }
}

@GraphQLInterface(name = "Node", implementationAutoDiscovery = true)
interface Node {
    val id: String
}

data class AddTodoPayload(
    val todoEdge: TodoEdge,
    val user: User,
)

data class AddTodo(
    val text: String,
    val userId: String,
)

data class ChangeTodoStatusPayload(
    val todo: Todo?,
    val user: User,
)

data class ChangeTodoStatus(
    val complete: Boolean,
    val id: String,
    val userId: String,
)

data class MarkAllTodosPayload(
    val changedTodos: List<Todo>,
    val user: User,
)

data class MarkAllTodos(
    val complete: Boolean,
    val userId: String,
)

data class RemoveCompletedTodosPayload(
    val deletedTodoIds: List<ID>,
    val user: User,
)

data class RemoveCompletedTodos(
    //  Value: {userId=me} could not be parsed into an instance of com.ignite.solarb.graphqlspqr.relay.RemoveCompletedTodos
    val userId: String = "",
)

data class RemoveTodoPayload(
    val deletedTodoId: ID,
    val user: User,
)

@GraphQLId //  Invalid use of @deleteEdge on field 'deletedTodoIds'. Expected field type 'ID', got '[String]'.
data class ID(
    val id: String,
)

data class RemoveTodo(
    val id: String,
    val userId: String,
)

data class RenameTodoPayload(
    val todo: Todo,
)

data class RenameTodo(
    val id: String,
    val text: String,
)
