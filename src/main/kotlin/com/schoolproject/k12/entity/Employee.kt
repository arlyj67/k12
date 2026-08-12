package com.schoolproject.k12.entity

import com.schoolproject.k12.model.EmployeeStatus
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
@Table(name = "employees")
class Employee(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    // Let JPA own ID generation — no Kotlin-level default needed
    val id: UUID? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    val school: School,

    @Column(name = "employee_number", unique = true, nullable = false, length = 20)
    val employeeNumber: String,

    @Column(name = "first_name", nullable = false, length = 50)
    val firstName: String,

    @Column(name = "middle_name", length = 50)
    val middleName: String? = null,

    @Column(name = "last_name", nullable = false, length = 50)
    val lastName: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,

    @Column(name = "contact_number", length = 20)
    val contactNumber: String? = null,

    @Column(length = 255)
    val address: String? = null,

    @Column(length = 255)
    val email: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: EmployeeStatus = EmployeeStatus.ACTIVE,

    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()

) {
    val fullName: String
        get() = listOfNotNull(firstName, middleName, lastName).joinToString(" ")

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