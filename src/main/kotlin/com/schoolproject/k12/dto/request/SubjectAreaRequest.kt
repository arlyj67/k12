package com.schoolproject.k12.dto.request

import java.util.UUID

data class SubjectAreaRequest(
    val schoolId: UUID,
    val areaName: String,
    val description: String? = null,
    val isActive: Boolean = true
)
