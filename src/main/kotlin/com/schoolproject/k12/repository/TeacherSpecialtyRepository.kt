package com.schoolproject.k12.repository

import com.schoolproject.k12.entity.TeacherSpecialty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TeacherSpecialtyRepository : JpaRepository<TeacherSpecialty, UUID> {
    fun findByEmployeeId(employeeId: UUID): List<TeacherSpecialty>
    fun findBySubjectAreaId(subjectAreaId: UUID): List<TeacherSpecialty>
    fun existsByEmployeeIdAndSubjectAreaId(employeeId: UUID, subjectAreaId: UUID): Boolean
    fun deleteByEmployeeIdAndSubjectAreaId(employeeId: UUID, subjectAreaId: UUID)
}