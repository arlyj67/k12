package com.schoolproject.k12.dto.request

import jakarta.validation.constraints.PositiveOrZero
import java.util.UUID

data class GradeRequest(
    val schoolId: UUID,
    val studentId: UUID,
    val gradingCriteriaId: UUID,
    val encodedById: UUID,

    @field:PositiveOrZero(message = "Written works score must not be negative")
    val writtenWorksScore: Double,

    @field:PositiveOrZero(message = "Performance task score must not be negative")
    val performanceTaskScore: Double,

    @field:PositiveOrZero(message = "Quarterly assessment score must not be negative")
    val quarterlyAssessmentScore: Double
)