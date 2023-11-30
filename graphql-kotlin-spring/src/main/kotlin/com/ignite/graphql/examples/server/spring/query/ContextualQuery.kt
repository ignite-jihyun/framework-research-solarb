/*
 * Copyright 2022 Expedia, Inc
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

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import com.ignite.graphql.examples.server.spring.model.ContextualResponse
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

/**
 * Example usage of GraphQLContext. Since `DataFetchingEnvironment` is passed as the argument
 * of [ContextualQuery.contextualQuery], it will not appear in the schema and be populated at runtime.
 */
@Component
class ContextualQuery : Query {

    @GraphQLDescription("query that uses GraphQLContext context")
    fun contextualQuery(
        @GraphQLDescription("some value that will be returned to the user")
        value: Int,
        env: DataFetchingEnvironment
    ): ContextualResponse = ContextualResponse(value, env.graphQlContext.getOrDefault("myCustomValue", "defaultValue"))
}