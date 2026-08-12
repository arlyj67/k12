package com.schoolproject.k12.dto.response

import java.util.UUID

data class SubjectAreaResponse(
    val id: UUID?,
    val schoolId: UUID,
    val areaName: String,
    val description: String?,
    val isActive: Boolean
)
