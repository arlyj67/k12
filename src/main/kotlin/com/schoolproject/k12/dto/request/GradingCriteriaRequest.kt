package com.schoolproject.k12.dto.request

import com.schoolproject.k12.model.GradingPeriod
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.util.UUID

data class GradingCriteriaRequest(
    val schoolId: UUID,
    val subjectId: UUID,
    val sectionId: UUID,
    val createdById: UUID,

    @field:NotBlank(message = "School year must not be blank")
    val schoolYear: String,

    val gradingPeriod: GradingPeriod,

    @field:Positive(message = "Written works total must be greater than zero")
    val writtenWorksTotal: Double,

    @field:Positive(message = "Performance task total must be greater than zero")
    val performanceTaskTotal: Double,

    @field:Positive(message = "Quarterly assessment total must be greater than zero")
    val quarterlyAssessmentTotal: Double
) {
    init {
        require(writtenWorksTotal > 0) {
            "writtenWorksTotal must be greater than zero"
        }
        require(performanceTaskTotal > 0) {
            "performanceTaskTotal must be greater than zero"
        }
        require(quarterlyAssessmentTotal > 0) {
            "quarterlyAssessmentTotal must be greater than zero"
        }
    }
}