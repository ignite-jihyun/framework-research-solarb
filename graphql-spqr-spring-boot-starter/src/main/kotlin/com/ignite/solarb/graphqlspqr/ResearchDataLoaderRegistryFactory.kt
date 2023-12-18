package com.ignite.solarb.graphqlspqr

import com.ignite.research.Board
import com.ignite.research.Comment
import com.ignite.research.CommentRepository
import io.leangen.graphql.spqr.spring.autoconfigure.DataLoaderRegistryFactory
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import org.dataloader.DataLoaderRegistry
import org.dataloader.MappedBatchLoader
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class ResearchDataLoaderRegistryFactory(
    val commentRepository: CommentRepository,
) : DataLoaderRegistryFactory {
    override fun createDataLoaderRegistry(): DataLoaderRegistry {
        return DataLoaderRegistry()
            .register("commentsByDataLoader", DataLoaderFactory.newMappedDataLoader(commentsByDataLoader, DataLoaderOptions.newOptions()))
            .register("lastThreeComments", DataLoaderFactory.newMappedDataLoader(lastThreeComments, DataLoaderOptions.newOptions()))
            .register("commentsByDataLoaderByBoardId", DataLoaderFactory.newMappedDataLoader(commentsByDataLoaderByBoardId, DataLoaderOptions.newOptions()))
            .register("lastThreeCommentsByBoardId", DataLoaderFactory.newMappedDataLoader(lastThreeCommentsByBoardId, DataLoaderOptions.newOptions()))
    }

    val commentsByDataLoader: MappedBatchLoader<Board, List<Comment>> =
        MappedBatchLoader { boards ->
            CompletableFuture.supplyAsync {
                val commentsByBoardId = commentRepository.findByBoardIn(boards.toList()).groupBy { it.board.id }
                boards.associateWith { board -> commentsByBoardId[board.id] ?: emptyList() }
            }
        }

    val lastThreeComments: MappedBatchLoader<Board, List<Comment>> =
        MappedBatchLoader { boards ->
            CompletableFuture.supplyAsync {
                val commentsByBoardId: Map<Long, List<Comment>> = commentRepository.findLastThreeByPostIdIn(boards.mapNotNull { it.id }).groupBy { it.board.id!! }
                boards.associateWith { board -> commentsByBoardId[board.id] ?: emptyList() }
            }
        }

    val commentsByDataLoaderByBoardId: MappedBatchLoader<Long, List<Comment>> =
        MappedBatchLoader { boardIds ->
            CompletableFuture.supplyAsync {
                commentRepository.findByBoardIds(boardIds.toList()).groupBy { it.board.id }
            }
        }

    val lastThreeCommentsByBoardId: MappedBatchLoader<Long, List<Comment>> =
        MappedBatchLoader { boardIds ->
            CompletableFuture.supplyAsync {
                commentRepository.findLastThreeByPostIdIn(boardIds.toList()).groupBy { it.board.id }
            }
        }
}
