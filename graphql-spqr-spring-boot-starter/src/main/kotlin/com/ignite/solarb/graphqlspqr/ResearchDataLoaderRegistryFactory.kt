package com.ignite.solarb.graphqlspqr

import com.ignite.research.Board
import com.ignite.research.Comment
import com.ignite.research.CommentRepository
import io.leangen.graphql.spqr.spring.autoconfigure.DataLoaderRegistryFactory
import org.dataloader.*
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class ResearchDataLoaderRegistryFactory(
    val commentRepository: CommentRepository
) : DataLoaderRegistryFactory {
    override fun createDataLoaderRegistry(): DataLoaderRegistry {
        return DataLoaderRegistry()
            .register("commentsByDataLoader", DataLoaderFactory.newDataLoader(commentsByDataLoader, DataLoaderOptions.newOptions()))
            .register("lastThreeComments", DataLoaderFactory.newMappedDataLoader(lastThreeComments, DataLoaderOptions.newOptions()))
    }

    private val commentsByDataLoader: BatchLoader<Board, List<Comment>> =
        BatchLoader { boards ->
            CompletableFuture.supplyAsync {
                commentRepository.findByBoardIn(boards).groupBy { it.board }.values.toList()
            }
        }

    private val lastThreeComments: MappedBatchLoader<Board, List<Comment>> =
        MappedBatchLoader { boards ->
            CompletableFuture.supplyAsync {
                commentRepository.findLastThreeByPostIdIn(boards.mapNotNull { it.id }).groupBy { it.board }
            }
        }
}
