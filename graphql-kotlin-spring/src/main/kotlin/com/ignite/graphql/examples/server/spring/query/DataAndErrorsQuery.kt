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

import com.expediagroup.graphql.server.operations.Query
import graphql.GraphQLError
import graphql.GraphqlErrorException
import graphql.execution.DataFetcherResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class DataAndErrorsQuery : Query {

    fun returnDataAndErrors(): DataFetcherResult<String?> {
        return DataFetcherResult.newResult<String>()
            .data("Hello from data fetcher")
            .error(getError())
            .build()
    }

    fun completableFutureDataAndErrors(): CompletableFuture<DataFetcherResult<String?>> {
        val dataFetcherResult = DataFetcherResult.newResult<String>()
            .data("Hello from data fetcher")
            .error(getError())
            .build()
        return CompletableFuture.completedFuture(dataFetcherResult)
    }

    private fun getError(): GraphQLError = GraphqlErrorException.newErrorException()
        .cause(RuntimeException("data and errors"))
        .message("data and errors")
        .build()
}
