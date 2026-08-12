package com.schoolproject.k12.entity

import com.schoolproject.k12.model.Gender
import com.schoolproject.k12.model.StudentStatus
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
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "students")
class Student (

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    val school: School,

    @Column(name = "student_number", unique = true, nullable = false, length = 20)
    val studentNumber: String,

    @Column(name = "first_name", nullable = false, length = 50)
    val firstName: String,

    @Column(name = "middle_name", length = 50)
    val middleName: String? = null,

    @Column(name = "last_name", nullable = false, length = 50)
    val lastName: String,

    @Column(name = "date_of_birth", nullable = false)
    val dateOfBirth: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val gender: Gender,

    @Column(length = 255)
    val address: String? = null,

    @Column(length = 255)
    val email: String? = null,

    @Column(name = "contact_number", length = 20)
    val contactNumber: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: StudentStatus = StudentStatus.PENDING,

    @Column(name = "student_level", nullable = false)
    val studentLevel: Int,

    @Column(name = "school_year", nullable = false, length = 20)
    @field:NotBlank(message = "School year must not be blank")
    val schoolYear: String,

    @Column(name = "guardian_name", nullable = false, length = 100)
    val guardianName: String,

    @Column(name = "guardian_contact", nullable = false, length = 20)
    val guardianContact: String,

    @Column(name = "guardian_email", length = 100)
    val guardianEmail: String? = null,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    val fullName: String
        get() = listOfNotNull(firstName, middleName, lastName).joinToString(" ")

    fun requireId(): UUID = id
        ?: error("Student has not been persisted yet — id is null")
}