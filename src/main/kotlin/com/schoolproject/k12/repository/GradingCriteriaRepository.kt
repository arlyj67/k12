package com.schoolproject.k12.repository

import com.schoolproject.k12.entity.GradingCriteria
import com.schoolproject.k12.model.GradingPeriod
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface GradingCriteriaRepository : JpaRepository<GradingCriteria, UUID> {

    fun findBySectionId(sectionId: UUID): List<GradingCriteria>
    fun findBySubjectId(subjectId: UUID): List<GradingCriteria>
    fun findBySchoolId(schoolId: UUID): List<GradingCriteria>
    fun findBySectionIdAndSchoolYear(sectionId: UUID, schoolYear: String): List<GradingCriteria>
    fun findBySectionIdAndGradingPeriod(sectionId: UUID, gradingPeriod: GradingPeriod): List<GradingCriteria>

    // Check if criteria already exists for same subject, section, school year and grading period
    fun existsBySubjectIdAndSectionIdAndSchoolYearAndGradingPeriod(
        subjectId: UUID,
        sectionId: UUID,
        schoolYear: String,
        gradingPeriod: GradingPeriod
    ): Boolean
}