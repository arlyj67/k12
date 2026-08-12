package com.schoolproject.k12.dto.response

import com.schoolproject.k12.model.Gender
import com.schoolproject.k12.model.StudentStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class StudentResponse (
    val id: UUID?,
    val schoolName: String,
    val studentNumber: String,
    val fullName: String,
    val dateOfBirth: LocalDate,
    val email: String?,
    val gender: Gender,
    val address: String?,
    val contactNumber: String?,
    val status: StudentStatus,
    val studentLevel: Int,
    val schoolYear: String,
    val guardianName: String,
    val guardianContact: String,
    val guardianEmail: String,
    val createdAt: LocalDateTime
)

data class StudentListResponse (
    val id: UUID?,
    val studentNumber: String,
    val fullName: String
)