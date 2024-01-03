package com.ignite.research

import io.leangen.graphql.annotations.GraphQLQuery
import io.leangen.graphql.annotations.types.GraphQLType
import jakarta.persistence.Embedded
import jakarta.persistence.Entity

@GraphQLType(description = "Board2")
@Entity(name = "`board2`")
class Board2 : PrimaryKeyEntity() {
    @GraphQLQuery(description = "title")
    var title: String = ""

    @GraphQLQuery(description = "content")
    var content: String = ""


    @Embedded
    var audit: Audit = Audit()

    fun isNew(): Boolean = id == null
}
