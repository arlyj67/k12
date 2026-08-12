package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.dto.request.GradingCriteriaRequest
import com.schoolproject.k12.dto.response.GradingCriteriaResponse
import com.schoolproject.k12.entity.GradingCriteria
import com.schoolproject.k12.mapper.GradingCriteriaMapper
import com.schoolproject.k12.model.GradingPeriod
import com.schoolproject.k12.repository.EmployeeRepository
import com.schoolproject.k12.repository.GradingCriteriaRepository
import com.schoolproject.k12.repository.SchoolRepository
import com.schoolproject.k12.repository.SectionRepository
import com.schoolproject.k12.repository.SubjectRepository
import com.schoolproject.k12.service.GradingCriteriaService
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class GradingCriteriaServiceImpl(
    private val gradingCriteriaRepository: GradingCriteriaRepository,
    private val schoolRepository: SchoolRepository,
    private val subjectRepository: SubjectRepository,
    private val sectionRepository: SectionRepository,
    private val employeeRepository: EmployeeRepository,
    private val gradingCriteriaMapper: GradingCriteriaMapper
) : GradingCriteriaService {

    override fun createCriteria(request: GradingCriteriaRequest): GradingCriteriaResponse {
        val school = schoolRepository.findById(request.schoolId)
            .orElseThrow { EntityNotFoundException("School not found with id: ${request.schoolId}") }

        val subject = subjectRepository.findById(request.subjectId)
            .orElseThrow { EntityNotFoundException("Subject not found with id: ${request.subjectId}") }

        val section = sectionRepository.findById(request.sectionId)
            .orElseThrow { EntityNotFoundException("Section not found with id: ${request.sectionId}") }

        val createdBy = employeeRepository.findById(request.createdById)
            .orElseThrow { EntityNotFoundException("Employee not found with id: ${request.createdById}") }

        // Check if criteria already exists for same subject, section, school year and grading period
        if (gradingCriteriaRepository.existsBySubjectIdAndSectionIdAndSchoolYearAndGradingPeriod(
                request.subjectId,
                request.sectionId,
                request.schoolYear,
                request.gradingPeriod
            )
        ) {
            throw IllegalArgumentException(
                "Grading criteria already exists for this subject, section, school year and grading period"
            )
        }

        val criteria = gradingCriteriaRepository.save(
            gradingCriteriaMapper.toEntity(request, school, subject, section, createdBy)
        )

        return gradingCriteriaMapper.toResponse(criteria)
    }

    override fun updateCriteria(id: UUID, request: GradingCriteriaRequest): GradingCriteriaResponse {
        val existing = gradingCriteriaRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Grading criteria not found with id: $id") }

        val updated = gradingCriteriaRepository.save(
            GradingCriteria(
                id = existing.id,
                school = existing.school,
                subject = existing.subject,
                section = existing.section,
                schoolYear = existing.schoolYear,
                gradingPeriod = existing.gradingPeriod,
                writtenWorksTotal = request.writtenWorksTotal,
                performanceTaskTotal = request.performanceTaskTotal,
                quarterlyAssessmentTotal = request.quarterlyAssessmentTotal,
                createdBy = existing.createdBy,
                createdAt = existing.createdAt
            )
        )

        return gradingCriteriaMapper.toResponse(updated)
    }

    override fun getCriteriaById(id: UUID): GradingCriteriaResponse =
        gradingCriteriaMapper.toResponse(
            gradingCriteriaRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Grading criteria not found with id: $id") }
        )

    override fun getCriteriaBySection(sectionId: UUID): List<GradingCriteriaResponse> =
        gradingCriteriaMapper.toResponseList(
            gradingCriteriaRepository.findBySectionId(sectionId)
        )

    override fun getCriteriaBySubject(subjectId: UUID): List<GradingCriteriaResponse> =
        gradingCriteriaMapper.toResponseList(
            gradingCriteriaRepository.findBySubjectId(subjectId)
        )

    override fun getCriteriaBySectionAndSchoolYear(
        sectionId: UUID,
        schoolYear: String
    ): List<GradingCriteriaResponse> =
        gradingCriteriaMapper.toResponseList(
            gradingCriteriaRepository.findBySectionIdAndSchoolYear(sectionId, schoolYear)
        )

    override fun getCriteriaBySectionAndGradingPeriod(
        sectionId: UUID,
        gradingPeriod: GradingPeriod
    ): List<GradingCriteriaResponse> =
        gradingCriteriaMapper.toResponseList(
            gradingCriteriaRepository.findBySectionIdAndGradingPeriod(sectionId, gradingPeriod)
        )

    override fun deleteCriteria(id: UUID) {
        if (!gradingCriteriaRepository.existsById(id)) {
            throw EntityNotFoundException("Grading criteria not found with id: $id")
        }
        gradingCriteriaRepository.deleteById(id)
    }
}