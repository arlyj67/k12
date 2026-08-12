package com.schoolproject.k12.dto.response

import java.time.LocalDateTime
import java.util.UUID

data class SectionResponse(
    val id: UUID?,
    val schoolName: String,
    val sectionName: String,
    val studentLevel: Int,
    val schoolYear: String?,
    val roomNumber: String,
    val adviserName: String,
    val createdAt: LocalDateTime
)
