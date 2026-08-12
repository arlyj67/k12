package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.dto.request.GradeRequest
import com.schoolproject.k12.dto.response.GradeResponse
import com.schoolproject.k12.entity.Grade
import com.schoolproject.k12.mapper.GradeMapper
import com.schoolproject.k12.model.GradingPeriod
import com.schoolproject.k12.repository.EmployeeRepository
import com.schoolproject.k12.repository.GradingCriteriaRepository
import com.schoolproject.k12.repository.GradeRepository
import com.schoolproject.k12.repository.SchoolRepository
import com.schoolproject.k12.repository.StudentRepository
import com.schoolproject.k12.service.GradeService
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class GradeServiceImpl(
    private val gradeRepository: GradeRepository,
    private val gradingCriteriaRepository: GradingCriteriaRepository,
    private val studentRepository: StudentRepository,
    private val schoolRepository: SchoolRepository,
    private val employeeRepository: EmployeeRepository,
    private val gradeMapper: GradeMapper
) : GradeService {

    override fun encodeGrade(request: GradeRequest): GradeResponse {
        val school = schoolRepository.findById(request.schoolId)
            .orElseThrow { EntityNotFoundException("School not found with id: ${request.schoolId}") }

        val student = studentRepository.findById(request.studentId)
            .orElseThrow { EntityNotFoundException("Student not found with id: ${request.studentId}") }

        val criteria = gradingCriteriaRepository.findById(request.gradingCriteriaId)
            .orElseThrow { EntityNotFoundException("Grading criteria not found with id: ${request.gradingCriteriaId}") }

        val encodedBy = employeeRepository.findById(request.encodedById)
            .orElseThrow { EntityNotFoundException("Employee not found with id: ${request.encodedById}") }

        // Check if grade already exists
        if (gradeRepository.existsByStudentIdAndGradingCriteriaId(
                request.studentId, request.gradingCriteriaId
            )
        ) {
            throw IllegalArgumentException(
                "Grade already exists for this student and grading criteria"
            )
        }

        // Validate scores don't exceed totals
        validateScores(request, criteria.writtenWorksTotal, criteria.performanceTaskTotal, criteria.quarterlyAssessmentTotal)

        // Compute initial grade
        val initialGrade = computeInitialGrade(
            request.writtenWorksScore, criteria.writtenWorksTotal,
            request.performanceTaskScore, criteria.performanceTaskTotal,
            request.quarterlyAssessmentScore, criteria.quarterlyAssessmentTotal
        )

        // Compute transmuted grade
        val transmutedGrade = transmute(initialGrade)

        val grade = gradeRepository.save(
            gradeMapper.toEntity(request, school, student, criteria, encodedBy, initialGrade, transmutedGrade)
        )

        return gradeMapper.toResponse(grade)
    }

    override fun updateGrade(id: UUID, request: GradeRequest): GradeResponse {
        val existing = gradeRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Grade not found with id: $id") }

        val criteria = existing.gradingCriteria

        // Validate scores don't exceed totals
        validateScores(request, criteria.writtenWorksTotal, criteria.performanceTaskTotal, criteria.quarterlyAssessmentTotal)

        // Recompute grades
        val initialGrade = computeInitialGrade(
            request.writtenWorksScore, criteria.writtenWorksTotal,
            request.performanceTaskScore, criteria.performanceTaskTotal,
            request.quarterlyAssessmentScore, criteria.quarterlyAssessmentTotal
        )
        val transmutedGrade = transmute(initialGrade)

        val updated = gradeRepository.save(
            Grade(
                id = existing.id,
                school = existing.school,
                student = existing.student,
                gradingCriteria = existing.gradingCriteria,
                writtenWorksScore = request.writtenWorksScore,
                performanceTaskScore = request.performanceTaskScore,
                quarterlyAssessmentScore = request.quarterlyAssessmentScore,
                initialGrade = initialGrade,
                transmutedGrade = transmutedGrade,
                encodedBy = existing.encodedBy,
                encodedAt = existing.encodedAt,
                createdAt = existing.createdAt
            )
        )

        return gradeMapper.toResponse(updated)
    }

    override fun getGradeById(id: UUID): GradeResponse =
        gradeMapper.toResponse(
            gradeRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Grade not found with id: $id") }
        )

    override fun getGradesByStudent(studentId: UUID): List<GradeResponse> =
        gradeMapper.toResponseList(gradeRepository.findByStudentId(studentId))

    override fun getGradesByStudentAndSchoolYear(studentId: UUID, schoolYear: String): List<GradeResponse> =
        gradeMapper.toResponseList(
            gradeRepository.findByStudentIdAndGradingCriteriaSchoolYear(studentId, schoolYear)
        )

    override fun getGradesByStudentAndGradingPeriod(
        studentId: UUID,
        gradingPeriod: GradingPeriod
    ): List<GradeResponse> =
        gradeMapper.toResponseList(
            gradeRepository.findByStudentIdAndGradingCriteriaGradingPeriod(studentId, gradingPeriod)
        )

    override fun getGradesBySection(sectionId: UUID): List<GradeResponse> =
        gradeMapper.toResponseList(gradeRepository.findByGradingCriteriaSectionId(sectionId))

    override fun getGradesBySectionAndGradingPeriod(
        sectionId: UUID,
        gradingPeriod: GradingPeriod
    ): List<GradeResponse> =
        gradeMapper.toResponseList(
            gradeRepository.findByGradingCriteriaSectionIdAndGradingCriteriaGradingPeriod(
                sectionId, gradingPeriod
            )
        )

    override fun getGradesBySubjectAndSection(subjectId: UUID, sectionId: UUID): List<GradeResponse> =
        gradeMapper.toResponseList(
            gradeRepository.findByGradingCriteriaSubjectIdAndGradingCriteriaSectionId(
                subjectId, sectionId
            )
        )

    override fun getFinalGradesByStudent(studentId: UUID, schoolYear: String): Map<String, Double?> {
        val grades = gradeRepository.findByStudentIdAndSchoolYearOrderBySubject(studentId, schoolYear)

        // Group grades by subject name
        return grades.groupBy { it.gradingCriteria.subject.subjectName }
            .mapValues { (_, subjectGrades) ->
                // Only compute final grade if all 3 trimesters are encoded
                if (subjectGrades.size == GradingPeriod.entries.size) {
                    subjectGrades.map { it.transmutedGrade }.average()
                } else {
                    null
                }
            }
    }

    override fun deleteGrade(id: UUID) {
        if (!gradeRepository.existsById(id)) {
            throw EntityNotFoundException("Grade not found with id: $id")
        }
        gradeRepository.deleteById(id)
    }

    // --- Private helpers ---

    private fun validateScores(
        request: GradeRequest,
        wwTotal: Double,
        ptTotal: Double,
        qaTotal: Double
    ) {
        require(request.writtenWorksScore <= wwTotal) {
            "Written works score (${request.writtenWorksScore}) exceeds total ($wwTotal)"
        }
        require(request.performanceTaskScore <= ptTotal) {
            "Performance task score (${request.performanceTaskScore}) exceeds total ($ptTotal)"
        }
        require(request.quarterlyAssessmentScore <= qaTotal) {
            "Quarterly assessment score (${request.quarterlyAssessmentScore}) exceeds total ($qaTotal)"
        }
    }

    private fun computeInitialGrade(
        wwScore: Double, wwTotal: Double,
        ptScore: Double, ptTotal: Double,
        qaScore: Double, qaTotal: Double
    ): Double {
        val ww = (wwScore / wwTotal) * 25
        val pt = (ptScore / ptTotal) * 50
        val qa = (qaScore / qaTotal) * 25
        return ww + pt + qa
    }

    private fun transmute(initialGrade: Double): Double {
        return when {
            initialGrade >= 100.00 -> 100.0
            initialGrade >= 98.40 -> 99.0
            initialGrade >= 96.80 -> 98.0
            initialGrade >= 95.20 -> 97.0
            initialGrade >= 93.60 -> 96.0
            initialGrade >= 92.00 -> 95.0
            initialGrade >= 90.40 -> 94.0
            initialGrade >= 88.80 -> 93.0
            initialGrade >= 87.20 -> 92.0
            initialGrade >= 85.60 -> 91.0
            initialGrade >= 84.00 -> 90.0
            initialGrade >= 82.40 -> 89.0
            initialGrade >= 80.80 -> 88.0
            initialGrade >= 79.20 -> 87.0
            initialGrade >= 77.60 -> 86.0
            initialGrade >= 76.00 -> 85.0
            initialGrade >= 74.40 -> 84.0
            initialGrade >= 72.80 -> 83.0
            initialGrade >= 71.20 -> 82.0
            initialGrade >= 69.60 -> 81.0
            initialGrade >= 68.00 -> 80.0
            initialGrade >= 66.40 -> 79.0
            initialGrade >= 64.80 -> 78.0
            initialGrade >= 63.20 -> 77.0
            initialGrade >= 61.60 -> 76.0
            initialGrade >= 60.00 -> 75.0
            initialGrade >= 56.00 -> 74.0
            initialGrade >= 52.00 -> 73.0
            initialGrade >= 48.00 -> 72.0
            initialGrade >= 44.00 -> 71.0
            initialGrade >= 40.00 -> 70.0
            initialGrade >= 36.00 -> 69.0
            initialGrade >= 32.00 -> 68.0
            initialGrade >= 28.00 -> 67.0
            initialGrade >= 24.00 -> 66.0
            initialGrade >= 20.00 -> 65.0
            initialGrade >= 16.00 -> 64.0
            initialGrade >= 12.00 -> 63.0
            initialGrade >= 8.00  -> 62.0
            initialGrade >= 4.00  -> 61.0
            else                  -> 60.0
        }
    }
}