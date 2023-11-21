/*
 * Copyright 2023 Expedia, Inc
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

package com.ignite.graphql.examples.server.spring.dataloaders

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.ignite.graphql.examples.server.spring.model.Company
import graphql.GraphQLContext
import org.dataloader.DataLoaderFactory
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class CompanyDataLoader(private val service: CompanyService) : KotlinDataLoader<Int, Company> {
    companion object {
        const val name = "CompanyDataLoader"
    }

    override val dataLoaderName = name
    override fun getDataLoader(graphQLContext: GraphQLContext) =
        DataLoaderFactory.newDataLoader<Int, Company> { ids ->
            CompletableFuture.supplyAsync { service.getCompanies(ids) }
        }
}
