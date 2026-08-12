package com.schoolproject.k12.dto.response

import java.time.LocalDateTime
import java.util.UUID

data class SchoolResponse(
    val id: UUID?,
    val schoolName: String,
    val address: String,
    val contactNumber: String?,
    val email: String?,
    val principalName: String?,
    val isActive: Boolean,
    val createdAt: LocalDateTime
)