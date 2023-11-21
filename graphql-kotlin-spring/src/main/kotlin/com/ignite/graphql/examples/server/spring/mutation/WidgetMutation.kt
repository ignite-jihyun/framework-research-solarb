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

package com.ignite.graphql.examples.server.spring.mutation

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import com.ignite.graphql.examples.server.spring.model.Widget
import org.springframework.stereotype.Component

/**
 * Simple widget mutation that shows that same objects can be used for input and output GraphQL types.
 */
@Component
class WidgetMutation : Mutation {

    @GraphQLDescription("modifies passed in widget so it doesn't have null value")
    fun processWidget(@GraphQLDescription("widget to be modified") widget: Widget): Widget {
        if (null == widget.value) {
            widget.value = 42
        }
        return widget
    }

    fun processWidgetList(widgets: List<Widget>): List<Widget> {
        widgets.forEach {
            if (null == it.value) {
                it.value = 42
            }
        }

        return widgets
    }
}
