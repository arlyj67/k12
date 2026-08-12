package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.request.GradingCriteriaRequest
import com.schoolproject.k12.dto.response.GradingCriteriaResponse
import com.schoolproject.k12.entity.Employee
import com.schoolproject.k12.entity.GradingCriteria
import com.schoolproject.k12.entity.School
import com.schoolproject.k12.entity.Section
import com.schoolproject.k12.entity.Subject
import org.springframework.stereotype.Component

@Component
class GradingCriteriaMapper {

    fun toEntity(
        dto: GradingCriteriaRequest,
        school: School,
        subject: Subject,
        section: Section,
        createdBy: Employee
    ): GradingCriteria {
        return GradingCriteria(
            school = school,
            subject = subject,
            section = section,
            schoolYear = dto.schoolYear,
            gradingPeriod = dto.gradingPeriod,
            writtenWorksTotal = dto.writtenWorksTotal,
            performanceTaskTotal = dto.performanceTaskTotal,
            quarterlyAssessmentTotal = dto.quarterlyAssessmentTotal,
            createdBy = createdBy
        )
    }

    fun toResponse(criteria: GradingCriteria): GradingCriteriaResponse {
        return GradingCriteriaResponse(
            id = criteria.requireId(),
            schoolId = criteria.school.requireId(),
            schoolName = criteria.school.schoolName,
            subjectId = criteria.subject.requireId(),
            subjectName = criteria.subject.subjectName,
            subjectCode = criteria.subject.subjectCode,
            sectionId = criteria.section.requireId(),
            sectionName = criteria.section.sectionName,
            schoolYear = criteria.schoolYear,
            gradingPeriod = criteria.gradingPeriod,
            writtenWorksTotal = criteria.writtenWorksTotal,
            performanceTaskTotal = criteria.performanceTaskTotal,
            quarterlyAssessmentTotal = criteria.quarterlyAssessmentTotal,
            createdById = criteria.createdBy.id
                ?: error("Employee has not been persisted yet — id is null"),
            createdByName = criteria.createdBy.fullName,
            createdAt = criteria.createdAt,
            updatedAt = criteria.updatedAt
        )
    }

    fun toResponseList(criteriaList: List<GradingCriteria>): List<GradingCriteriaResponse> =
        criteriaList.map { toResponse(it) }
}