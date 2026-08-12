package com.schoolproject.k12.dto.request

import java.util.UUID

data class SectionRequest (
    val schoolId: UUID,
    val sectionName: String,
    val studentLevel: Int,
    val schoolYear: String,
    val roomId: UUID,
    val adviserId: UUID
)