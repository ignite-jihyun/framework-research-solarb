package com.ignite.solarb.graphqlspqr

import com.ignite.research.Board2
import com.ignite.research.BoardRepository2
import io.leangen.graphql.annotations.GraphQLQuery
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi
import org.springframework.stereotype.Service

@GraphQLApi
@Service
class GraphqlSpqrService(
    val boardRepository2: BoardRepository2,
) {
    @GraphQLQuery(description = "get board2 with dummy input")
    fun board2s(board2: Board2): List<Board2> {
        return boardRepository2.findAll()
    }
}
