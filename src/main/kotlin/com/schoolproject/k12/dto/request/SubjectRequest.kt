package com.schoolproject.k12.dto.request

import java.util.UUID

data class SubjectRequest (
    val schoolId: UUID,
    val subjectAreaId: UUID,
    val subjectCode: String,
    val subjectName: String,
    val description: String? = null,
    val studentLevel: Int,
    val schoolYear: String,
)