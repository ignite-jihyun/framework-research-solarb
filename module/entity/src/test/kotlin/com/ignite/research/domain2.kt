package com.ignite.research

import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.NaturalId
import org.springframework.data.jpa.repository.JpaRepository

/**
 * This is how to define an entity according to the JPA specification:
 *  1. default no-argument constructor;
 *  2. the class and all properties are open and non-final.
 */
@Table(name = "project")
@Entity
open class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    /**
     * Use lateinit on fields that are guaranteed to be non-null in the DB.
     * This allows the use of Kotlin nullability mechanism without removing the no-arg constructor.
     * NOTE: lateinit does not work with primitive types such as Long
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    open lateinit var client: Client
}
interface ProjectRepository : JpaRepository<Project, Long>

/**
 * Compiler plugins allow us to write Kotlin-style code that is compatible with JPA.
 *
 * To reduce boilerplate of writing "open" for each class and property,
 * use the all-open compiler plugin, which will do this automatically.
 * See how it's configured in the pom.xml file, and read more about it here: https://kotlinlang.org/docs/all-open-plugin.html
 *
 * Defining mandatory and optional properties in the constructor gives us better control of how the entities are created.
 * However, all entities are required to have a no-args constructor by the JPA specification.
 * kotlin-jpa plugin adds a no-args constructor to all classes marked with @Entity, @Embeddable or @MappedSuperclass.
 * Read more: https://kotlinlang.org/docs/no-arg-plugin.html#jpa-support
 */
@Table(name = "client")
@Entity
class Client(
    @Column(name = "name", nullable = false)
    var name: String,
) {
    /**
     * The id is generated by the DB and cannot be set by the developer,
     * so we exclude it from the constructor and define it as 'val'.
     * NOTE: Technically, this breaks the JPA specification and might not work with some JPA providers.
     * For example, it works in Hibernate and does not in EclipseLink.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null

    @OneToMany(mappedBy = "client")
    var projects: MutableSet<Project> = mutableSetOf()

    @OneToMany(mappedBy = "client")
    var contacts: MutableSet<Contact> = mutableSetOf()

    /**
     * Since the id can only be set by the DB, this method will return true only if the entity has been saved.
     */
    fun isNew(): Boolean = id == null
}

interface ClientRepository : JpaRepository<Client, Long>

/**
 * When using data classes for entities, ALWAYS override equals(), hashCode() and toString().
 * The default implementations include all the fields from the primary constructor, which can:
 * a) break equals()/hashCode() when the fields are mutable;
 * b) load all associations and cause performance issues or LazyInitializationException.
 *
 * Data classes still provide decomposition and copy().
 */
@Table(name = "contact")
@Entity
data class Contact(
    /**
     * If the entity has a natural or client-generated id, it makes sense to put it in the constructor,
     * so the object cannot be created without it.
     *
     * Since it does not change during the entity lifecycle, it can be used in equals() and hashCode().
     */
    @Id
    @NaturalId
    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    var client: Client,
) {
    /**
     * Implement equals() and hashCode() according to:
     * a) This guide by Thorben Janssen: https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/
     * b) This guide by Vlad Mihalcea: https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Contact

        return email == other.email
    }

    override fun hashCode(): Int = email.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(email = $email , name = $name )"
    }
}
interface ContactRepository : JpaRepository<Contact, String>