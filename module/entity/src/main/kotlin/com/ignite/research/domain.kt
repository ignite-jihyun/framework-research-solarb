package com.ignite.research

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.OneToMany
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

@Embeddable
class Audit(
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "created_by", updatable = false)
    val createdBy: String = "",
    @Column(name = "updated_by")
    var updatedBy: String = "",
)

@MappedSuperclass
abstract class PrimaryKeyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
) {
    // https://vmaks.github.io/2019/11/27/how-to-implement-equals-hashcode-for-kotlin-entity/
    @Override
    override fun hashCode(): Int {
        return 13
    }

    @Override
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as PrimaryKeyEntity
        return id == other.id
    }
}

@Entity(name = "`board`")
class Board : PrimaryKeyEntity() {
    var title: String = ""
    var content: String = ""

    @OneToMany(mappedBy = "board")
    var comments: MutableSet<Comment> = mutableSetOf()

    @Embedded
    var audit: Audit = Audit()

    fun isNew(): Boolean = id == null
}

interface BoardRepository : JpaRepository<Board, Long>

@Entity
class Comment : PrimaryKeyEntity() {
    var content: String = ""

    @Column(name = "parent_id")
    var parentId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    lateinit var board: Board

    @Embedded
    var audit: Audit = Audit()
}

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByBoard(board: Board): List<Comment>

    fun findByBoardIn(boards: List<Board>): List<Comment>

    @Query("select c from Comment c where c.board.id in ?1")
    fun findByBoardIds(ids: List<Long>): List<Comment>

    @Query(
        value = """
        WITH ranked_comments AS (
        SELECT c.*, ROW_NUMBER() OVER (PARTITION BY c.board_id ORDER BY c.id DESC) as row_num
        FROM comment c WHERE c.board_id IN :boardIds
                                                                                                            )
        SELECT *
        FROM ranked_comments
        WHERE row_num <= 3
        order by id desc
        """,
        nativeQuery = true,
    )
    fun findLastThreeByPostIdIn(boardIds: List<Long>): List<Comment>
}

@Entity(name = "`user`")
class ResearchUser(
    var userId: String = "",
    var name: String = "",
    var password: String = "",
    @Embedded
    val audit: Audit = Audit(),
) : PrimaryKeyEntity()

interface UserRepository : JpaRepository<ResearchUser, Long>
