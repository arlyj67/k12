package com.schoolproject.k12.repository

import com.schoolproject.k12.entity.Section
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SectionRepository : JpaRepository<Section, UUID> {
    fun findBySchoolId(schoolId: UUID): List<Section>
    fun findBySchoolIdAndSchoolYear(schoolId: UUID, schoolYear: String): List<Section>
    fun findBySchoolIdAndStudentLevel(schoolId: UUID, studentLevel: Int): List<Section>
    fun findByAdviserId(adviserId: UUID): List<Section>
    fun existsBySchoolIdAndSectionNameAndSchoolYear(schoolId: UUID, sectionName: String, schoolYear: String): Boolean
}