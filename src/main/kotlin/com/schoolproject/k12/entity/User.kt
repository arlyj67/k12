package com.schoolproject.k12.entity

import com.schoolproject.k12.model.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    // Nullable before persist — JPA assigns on save. Use requireId() for safe access.
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    val school: School,

    @Column(unique = true, nullable = false, length = 50)
    val username: String,

    // Unique constraint is enforced at DB level; nullable allows social/SSO accounts without email
    @Column(unique = true, nullable = true, length = 100)
    val email: String? = null,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    val employee: Employee? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()

) {
    /**
     * Returns the id after the entity has been persisted.
     * Throws [IllegalStateException] if called on an unpersisted entity.
     */
    fun requireId(): UUID = id
        ?: error("User has not been persisted yet — id is null")

    @PrePersist
    fun onPrePersist() {
        val now = LocalDateTime.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun onPreUpdate() {
        updatedAt = LocalDateTime.now()
    }
}