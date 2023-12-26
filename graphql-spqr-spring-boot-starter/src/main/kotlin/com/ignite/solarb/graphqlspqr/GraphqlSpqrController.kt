package com.ignite.solarb.graphqlspqr

import com.ignite.research.*
import io.leangen.graphql.annotations.GraphQLContext
import io.leangen.graphql.annotations.GraphQLEnvironment
import io.leangen.graphql.annotations.GraphQLMutation
import io.leangen.graphql.annotations.GraphQLQuery
import io.leangen.graphql.execution.ResolutionEnvironment
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi
import org.springframework.stereotype.Controller
import java.util.concurrent.CompletableFuture

@GraphQLApi
@Controller
class GraphqlSpqrController(
    val boardRepository: BoardRepository,
    val commentRepository: CommentRepository,
    var userRepository: UserRepository
) {
    @GraphQLQuery
    fun board(boardId: Long): Board {
        return boardRepository.findById(boardId).orElseThrow()
    }

    @GraphQLQuery
    fun boards(): List<Board> {
        return boardRepository.findAll()
    }

    @GraphQLQuery
    fun comment(commentId: Long): Comment {
        return commentRepository.findById(commentId).orElseThrow()
    }

    @GraphQLQuery
    fun comments(): List<Comment> {
        return commentRepository.findAll()
    }

    @GraphQLQuery
    fun comments(@GraphQLContext board: Board): List<Comment> {
        return commentRepository.findByBoard(board)
    }

    @GraphQLQuery
    fun user(userId: Long): User {
        return userRepository.findById(userId).orElseThrow()
    }

    @GraphQLQuery
    fun users(): List<User> {
        return userRepository.findAll()
    }

    @GraphQLMutation
    fun board(board: Board): Long {
        return boardRepository.save(board).id!!
    }

    @GraphQLMutation
    fun comment(comment: Comment): Long {
        return commentRepository.save(comment).id!!
    }

    @GraphQLMutation
    fun user(user: User): Long {
        return userRepository.save(user).id!!
    }

    @GraphQLQuery
    fun commentsByDataLoader(@GraphQLContext board: Board, @GraphQLEnvironment env: ResolutionEnvironment): CompletableFuture<List<Comment>> {
        return env.dataFetchingEnvironment.getDataLoader<Board, List<Comment>>("commentsByDataLoader").load(board)
    }

    @GraphQLQuery
    fun lastThreeComments(@GraphQLContext board: Board, @GraphQLEnvironment env: ResolutionEnvironment): CompletableFuture<List<Comment>> {
        return env.dataFetchingEnvironment.getDataLoader<Board, List<Comment>>("lastThreeComments").load(board)
    }

    @GraphQLQuery
    fun commentsByDataLoaderByBoardId(@GraphQLContext board: Board, @GraphQLEnvironment env: ResolutionEnvironment): CompletableFuture<List<Comment>> {
        return env.dataFetchingEnvironment.getDataLoader<Long, List<Comment>>("commentsByDataLoaderByBoardId").load(board.id)
    }

    @GraphQLQuery
    fun lastThreeCommentsByBoardId(@GraphQLContext board: Board, @GraphQLEnvironment env: ResolutionEnvironment): CompletableFuture<List<Comment>> {
        return env.dataFetchingEnvironment.getDataLoader<Long, List<Comment>>("lastThreeCommentsByBoardId").load(board.id)
    }
}
