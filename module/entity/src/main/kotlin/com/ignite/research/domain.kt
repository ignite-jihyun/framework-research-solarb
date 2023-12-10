package com.ignite.research


import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
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
    var updatedBy: String = ""
)

@MappedSuperclass
abstract class PrimaryKeyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)


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

interface CommentRepository : JpaRepository<Comment, Long>


@Entity(name = "`user`")
class User(
    var userId: String = "",

    var name: String = "",

    var password: String = "",
    @Embedded
    val audit: Audit = Audit()
) : PrimaryKeyEntity()

interface UserRepository : JpaRepository<User, Long>
