package com.ignite.solarb.graphqlspqr

import com.ignite.research.Board
import com.ignite.research.Comment
import com.ignite.research.CommentRepository
import org.dataloader.MappedBatchLoader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ResearchDataLoaderRegistryFactoryTest {

    @Mock
    lateinit var commentRepository: CommentRepository

    @InjectMocks
    lateinit var dataLoaderRegistryFactory: ResearchDataLoaderRegistryFactory


    @Test
    fun createDataLoaderRegistry() {
        val registry = dataLoaderRegistryFactory.createDataLoaderRegistry()
        assertNotNull(registry)
    }

    @Test
    fun commentsByDataLoader() {
        val boards = listOf(Board())
        val comments = listOf(Comment())
        `when`(commentRepository.findByBoardIn(boards)).thenReturn(comments)

        val batchLoader: MappedBatchLoader<Board, List<Comment>> = dataLoaderRegistryFactory.commentsByDataLoader
        val completionStage = batchLoader.load(boards.toSet())
        completionStage.whenComplete { result, _ ->
            assertEquals(1, result.size)
        }

        val boardIds = mutableSetOf(boards.first().id)
        val batchLoader2: MappedBatchLoader<Long, List<Comment>> = dataLoaderRegistryFactory.commentsByDataLoaderByBoardId
        val completionStage2 = batchLoader2.load(boardIds)
        completionStage2.whenComplete { result, _ ->
            assertEquals(1, result.size)
        }
    }

    @Test
    fun lastThreeComments() {
        val boards = mutableSetOf(Board().apply { id = 1L })
        val comments = listOf(Comment(), Comment())
        `when`(commentRepository.findLastThreeByPostIdIn(listOf(boards.first().id!!))).thenReturn(comments)

        val mappedBatchLoader: MappedBatchLoader<Board, List<Comment>> = dataLoaderRegistryFactory.lastThreeComments
        val completionStage = mappedBatchLoader.load(boards)
        completionStage.whenComplete { result, _ ->
            assertEquals(2, result.size)
        }

        val boardIds = mutableSetOf(1L)
        val mappedBatchLoader2: MappedBatchLoader<Long, List<Comment>> = dataLoaderRegistryFactory.lastThreeCommentsByBoardId
        val completionStage2 = mappedBatchLoader2.load(boardIds)
        completionStage2.whenComplete { result, _ ->
            assertEquals(2, result.size)
        }
    }
}
