package com.schoolproject.k12.dto.response

import java.time.LocalDateTime
import java.util.UUID

data class SubjectResponse(
    val id: UUID? = null,
    val schoolId: UUID,
    val schoolName: String,
    val subjectAreaId: UUID?,
    val areaName: String,
    val subjectCode: String,
    val subjectName: String,
    val description: String?,
    val studentLevel: Int,
    val schoolYear: String,
    val createdAt: LocalDateTime
)