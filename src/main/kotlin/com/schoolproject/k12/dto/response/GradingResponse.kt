package com.schoolproject.k12.dto.response

import com.schoolproject.k12.model.GradingPeriod
import java.time.LocalDateTime
import java.util.UUID

data class GradeResponse(
    val id: UUID,
    val schoolId: UUID,
    val schoolName: String,
    val studentId: UUID,
    val studentNumber: String,
    val studentName: String,
    val subjectId: UUID,
    val subjectName: String,
    val subjectCode: String,
    val sectionId: UUID,
    val sectionName: String,
    val schoolYear: String,
    val gradingPeriod: GradingPeriod,
    val writtenWorksScore: Double,
    val writtenWorksTotal: Double,
    val performanceTaskScore: Double,
    val performanceTaskTotal: Double,
    val quarterlyAssessmentScore: Double,
    val quarterlyAssessmentTotal: Double,
    val initialGrade: Double,
    val transmutedGrade: Double,
    val encodedById: UUID,
    val encodedByName: String,
    val encodedAt: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)