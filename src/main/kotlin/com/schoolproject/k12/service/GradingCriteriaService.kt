package com.schoolproject.k12.service

import com.schoolproject.k12.dto.request.GradingCriteriaRequest
import com.schoolproject.k12.dto.response.GradingCriteriaResponse
import com.schoolproject.k12.model.GradingPeriod
import java.util.UUID

interface GradingCriteriaService {
    fun createCriteria(request: GradingCriteriaRequest): GradingCriteriaResponse
    fun updateCriteria(id: UUID, request: GradingCriteriaRequest): GradingCriteriaResponse
    fun getCriteriaById(id: UUID): GradingCriteriaResponse
    fun getCriteriaBySection(sectionId: UUID): List<GradingCriteriaResponse>
    fun getCriteriaBySubject(subjectId: UUID): List<GradingCriteriaResponse>
    fun getCriteriaBySectionAndSchoolYear(sectionId: UUID, schoolYear: String): List<GradingCriteriaResponse>
    fun getCriteriaBySectionAndGradingPeriod(sectionId: UUID, gradingPeriod: GradingPeriod): List<GradingCriteriaResponse>
    fun deleteCriteria(id: UUID)
}