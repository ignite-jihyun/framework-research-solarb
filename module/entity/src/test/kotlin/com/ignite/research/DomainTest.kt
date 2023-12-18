package com.ignite.research

import org.hibernate.proxy.HibernateProxy
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DomainTest(
    @Autowired val boardRepository: BoardRepository,
    @Autowired val commentRepository: CommentRepository,
    @Autowired val userRepository: UserRepository
) {
    companion object {
        private val logger = LoggerFactory.getLogger(DomainTest::class.java)
    }

    @Test
    fun commentInitialized() {
        val comment = commentRepository.findById(1)
        assertTrue(comment.isPresent)
    }

    @Test
    fun lazyLoadEnabled() {
        val comment = commentRepository.findById(1).get()
        val board = comment.board
        logger.info { "Class used for the client reference: ${board::class.java}" }
        assertTrue(HibernateProxy::class.java.isAssignableFrom(board::class.java))
    }

    @Test
    fun hashcodeIsConsistent() {
        val board = boardRepository.findById(1).get()
        val comment = Comment().apply {
            this.content = "content"
            this.board = board
        }
        val hashSet = hashSetOf(comment)
        logger.info { comment.hashCode().toString() }

        commentRepository.save(comment)
        logger.info { comment.hashCode().toString() }

        assertTrue(comment in hashSet)
    }

    @Test
    fun valForIdTest() {
        val board = Board().apply {
            this.title = "title"
            this.content = "content"
        }
        assertTrue(board.isNew())
        boardRepository.save(board)

        assertFalse(board.isNew())
    }

    @Test
    fun lateInitWorks() {
        val comment = Comment().apply {
            board = Board().apply {
                title = "title"
                content = "content"
            }
        }
        assertEquals("title", comment.board.title)
    }

    @Test
    fun nPlus1() {
        commentRepository.findAll().forEach {
            logger.info { "comment: ${it.id}" }
        }
    }

    @Test
    fun `hash code`() {
        assertEquals(13, Board().hashCode())
    }

    @Test
    fun `test equals`() {
        val board = Board().apply { id = 1L }
        assertEquals(board, board)
        assertEquals(Board().apply { id = 1L }, Board().apply { id = 1L })
        assertNotEquals(Board().apply { id = 2L }, Board().apply { id = 1L })
        assertFalse(Comment().apply { id = 1L } == Board().apply { id = 1L })
    }
}
