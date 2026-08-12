package com.schoolproject.k12.dto.request

import com.schoolproject.k12.model.EmployeeStatus
import com.schoolproject.k12.model.Role
import java.util.UUID

data class EmployeeRequest (
    val schoolId: UUID,
    val employeeNumber: String,
    val firstName: String,
    val middleName: String? = null,
    val lastName: String,
    val role: Role,
    val contactNumber: String? = null,
    val address: String? = null,
    val email: String? = null,
    val status: EmployeeStatus = EmployeeStatus.ACTIVE
)