package com.schoolproject.k12.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "schools")
class School(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    // Nullable before persist — JPA assigns on save. Use requireId() for safe access.
    val id: UUID? = null,

    @Column(name = "school_name", nullable = false, length = 100)
    var schoolName: String,

    @Column(nullable = false, length = 255)
    var address: String,

    @Column(name = "contact_number", length = 20)
    var contactNumber: String? = null,

    @Column(length = 100)
    var email: String? = null,

    @Column(name = "principal_name", length = 100)
    var principalName: String? = null,

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
        ?: error("School has not been persisted yet — id is null")

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