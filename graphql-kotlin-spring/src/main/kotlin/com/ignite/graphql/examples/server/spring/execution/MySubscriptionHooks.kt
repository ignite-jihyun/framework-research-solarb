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

package com.ignite.graphql.examples.server.spring.execution

import com.expediagroup.graphql.generator.extensions.toGraphQLContext
import com.expediagroup.graphql.server.spring.subscriptions.ApolloSubscriptionHooks
import graphql.GraphQLContext
import org.springframework.web.reactive.socket.WebSocketSession

/**
 * A simple implementation of Apollo Subscription Lifecycle Events.
 */
class MySubscriptionHooks : ApolloSubscriptionHooks {

    override fun onConnectWithContext(
        connectionParams: Map<String, String>,
        session: WebSocketSession,
        graphQLContext: GraphQLContext
    ): GraphQLContext =
        mutableMapOf<Any, Any>().also { contextMap ->
            connectionParams["Authorization"]?.let { authValue ->
                contextMap["auth"] = authValue
            }
        }.toGraphQLContext()
}
