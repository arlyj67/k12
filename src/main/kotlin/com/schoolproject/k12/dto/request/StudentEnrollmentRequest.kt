package com.schoolproject.k12.dto.request

import com.schoolproject.k12.model.StudentStatus
import java.time.LocalDate
import java.util.UUID

data class StudentEnrollmentRequest(
    val schoolId: UUID,
    val studentId: UUID,
    val sectionId: UUID,
    val schoolYear: String,
    val enrollmentDate: LocalDate,
    val status: StudentStatus
)
