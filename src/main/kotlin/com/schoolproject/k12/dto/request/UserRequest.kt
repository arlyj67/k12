package com.schoolproject.k12.dto.request

import com.schoolproject.k12.model.Role
import java.util.UUID

data class UserRequest (
    val schoolId: UUID,
    val username: String,
    val email: String,
    val password: String,
    val role: Role,
    val isActive: Boolean = true
)