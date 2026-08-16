package com.schoolproject.k12.dto.request

import com.schoolproject.k12.model.Gender
import com.schoolproject.k12.model.StudentStatus
import java.time.LocalDate
import java.util.UUID

data class StudentRequest (
    val schoolId: UUID,
    val firstName: String,
    val middleName: String? = null,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val email: String,
    val gender: Gender,
    val address: String? = null,
    val contactNumber: String? = null,
    val status: StudentStatus = StudentStatus.PENDING,
    val studentLevel: Int,
    val schoolYear: String,
    val guardianName: String,
    val guardianContact: String,
    val guardianEmail: String,
    val gradeCompleted: String,
    val gradeCompletedYear: String
)