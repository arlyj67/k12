package com.schoolproject.k12.repository

import com.schoolproject.k12.entity.Grade
import com.schoolproject.k12.model.GradingPeriod
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface GradeRepository : JpaRepository<Grade, UUID> {

    fun findByStudentId(studentId: UUID): List<Grade>
    fun findByStudentIdAndGradingCriteriaSchoolYear(studentId: UUID, schoolYear: String): List<Grade>
    fun findByStudentIdAndGradingCriteriaGradingPeriod(studentId: UUID, gradingPeriod: GradingPeriod): List<Grade>
    fun findByGradingCriteriaSectionId(sectionId: UUID): List<Grade>
    fun findByGradingCriteriaSectionIdAndGradingCriteriaGradingPeriod(
        sectionId: UUID,
        gradingPeriod: GradingPeriod
    ): List<Grade>
    fun findByGradingCriteriaSubjectIdAndGradingCriteriaSectionId(
        subjectId: UUID,
        sectionId: UUID
    ): List<Grade>

    // Check if grade already exists for same student and grading criteria
    fun existsByStudentIdAndGradingCriteriaId(studentId: UUID, gradingCriteriaId: UUID): Boolean

    // Get all grades for a student per school year grouped for final grade computation
    @Query("""
        SELECT g FROM Grade g
        WHERE g.student.id = :studentId
        AND g.gradingCriteria.schoolYear = :schoolYear
        ORDER BY g.gradingCriteria.subject.id, g.gradingCriteria.gradingPeriod
    """)
    fun findByStudentIdAndSchoolYearOrderBySubject(
        studentId: UUID,
        schoolYear: String
    ): List<Grade>
}