package com.schoolproject.k12.dto.response

import com.schoolproject.k12.model.Role
import java.time.LocalDateTime
import java.util.UUID

data class UserResponse (
    val id: UUID?,
    val schoolName: String,
    val username: String,
    val email: String?,
    val role: Role,
    val isActive: Boolean,
    val createdAt: LocalDateTime
)