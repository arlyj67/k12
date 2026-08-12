package com.schoolproject.k12.dto.response

import com.schoolproject.k12.model.StudentStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class StudentEnrollmentResponse(
    val id: UUID,
    val schoolId: UUID,
    val schoolName: String,
    val studentId: UUID,
    val studentNumber: String,
    val studentName: String,
    val sectionId: UUID,
    val sectionName: String,
    val schoolYear: String,
    val enrollmentDate: LocalDate,
    val status: StudentStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)