package com.schoolproject.k12.dto.response

import com.schoolproject.k12.model.GradingPeriod
import java.time.LocalDateTime
import java.util.UUID

data class GradingCriteriaResponse(
    val id: UUID,
    val schoolId: UUID,
    val schoolName: String,
    val subjectId: UUID,
    val subjectName: String,
    val subjectCode: String,
    val sectionId: UUID,
    val sectionName: String,
    val schoolYear: String,
    val gradingPeriod: GradingPeriod,
    val writtenWorksTotal: Double,
    val performanceTaskTotal: Double,
    val quarterlyAssessmentTotal: Double,
    val createdById: UUID,
    val createdByName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)