package com.ignite.solarb.graphqlspqr.relay

import graphql.relay.DefaultConnectionCursor
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TodoModelTest {
    @Test
    fun `CustomPageInfo getCustomEndCursor getCustomStartCursor`() {
        val customPageInfo = CustomPageInfo(DefaultConnectionCursor("start"), DefaultConnectionCursor("end"), hasPreviousPage = false, hasNextPage = false)
        assertEquals("start", customPageInfo.getCustomStartCursor())
        assertEquals("end", customPageInfo.getCustomEndCursor())
    }

    @Test
    fun `TodoEdge getCustomCursor`() {
        val todoEdge = TodoEdge(Todo("id", "text", false), DefaultConnectionCursor("cursor"))
        assertEquals("cursor", todoEdge.getCustomCursor())
    }
}
