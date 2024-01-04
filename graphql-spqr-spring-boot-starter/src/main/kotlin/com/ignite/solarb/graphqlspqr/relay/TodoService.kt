package com.ignite.solarb.graphqlspqr.relay

import graphql.relay.DefaultConnectionCursor
import io.leangen.graphql.annotations.GraphQLContext
import io.leangen.graphql.annotations.GraphQLMutation
import io.leangen.graphql.annotations.GraphQLQuery
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service

@GraphQLApi
@Service
class TodoService {
    private var todoSequence = 0
    private val todos: MutableList<Todo> = ArrayList()

    @PostConstruct
    fun init() {
        addTodo("first todo")
        addTodo("second todo")
        addTodo("third todo")
    }

    private fun userMe() = User("me", "me", todos.size, todos.count { it.complete })

    private fun addTodo(text: String) {
        todos.add(Todo("${++todoSequence}", text, false))
    }

    private fun removeTodo(todoId: String) {
        todos.find { it.id == todoId }?.let {
            todos.remove(it)
        }
    }

    @GraphQLQuery
    fun user(id: String): User {
        return userMe()
    }

    @GraphQLQuery(description = "user relay")
    fun todos(
        @GraphQLContext user: User,
        status: String?,
        first: Int?,
        after: String?,
        before: String?,
        last: Int?,
    ): TodoConnection {
        val todoEdges = todos.map { TodoEdge(it, DefaultConnectionCursor(it.hashCode().toString())) }
        val pageInfo = CustomPageInfo(null, null, false, false)
        return TodoConnection(todoEdges.toList(), pageInfo)
    }

    @GraphQLMutation
    fun renameTodo(input: RenameTodo): RenameTodoPayload {
        todos.find { it.id == input.id }?.let {
            it.text = input.text
            return RenameTodoPayload(it)
        }
        throw NoSuchElementException("Todo not found id: ${input.id}")
    }

    @GraphQLMutation
    fun changeTodoStatus(input: ChangeTodoStatus): ChangeTodoStatusPayload {
        var changed: Todo? = null
        todos.find { it.id == input.id }?.let {
            it.complete = input.complete
            changed = it
        }
        return ChangeTodoStatusPayload(changed, userMe())
    }

    @GraphQLMutation
    fun markAllTodos(input: MarkAllTodos): MarkAllTodosPayload {
        val changed = ArrayList<Todo>()
        todos.forEach {
            if (input.complete != it.complete) {
                it.complete = input.complete
                changed.add(it)
            }
        }
        return MarkAllTodosPayload(changed, userMe())
    }

    @GraphQLMutation
    fun addTodo(input: AddTodo): AddTodoPayload {
        addTodo(input.text)
        val todoEdge = TodoEdge(todos.last(), DefaultConnectionCursor(todos.last().hashCode().toString()))
        return AddTodoPayload(todoEdge, userMe())
    }

    @GraphQLMutation
    fun removeTodo(input: RemoveTodo): RemoveTodoPayload {
        removeTodo(input.id)
        return RemoveTodoPayload(ID(input.id), userMe())
    }

    @GraphQLMutation
    fun removeCompletedTodos(input: RemoveCompletedTodos): RemoveCompletedTodosPayload {
        val completed = ArrayList<Todo>()
        todos.forEach {
            if (it.complete) {
                completed.add(it)
            }
        }
        completed.forEach {
            removeTodo(it.id)
        }
        return RemoveCompletedTodosPayload(completed.map { ID(it.id) }.toList(), userMe())
    }
}
