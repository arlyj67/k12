package com.schoolproject.k12.dto.response

import com.schoolproject.k12.model.EmployeeStatus
import com.schoolproject.k12.model.Role
import java.time.LocalDateTime
import java.util.UUID

data class EmployeeResponse(
    val id: UUID,
    val userId: UUID,
    val schoolId: UUID,
    val schoolName: String,
    val employeeNumber: String,
    val firstName: String,
    val middleName: String?,
    val lastName: String,
    val fullName: String,
    val role: Role,
    val contactNumber: String?,
    val address: String?,
    val email: String?,
    val status: EmployeeStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)