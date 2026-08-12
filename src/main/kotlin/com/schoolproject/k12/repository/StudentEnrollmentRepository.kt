package com.schoolproject.k12.repository

import com.schoolproject.k12.entity.StudentEnrollment
import com.schoolproject.k12.model.StudentStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface StudentEnrollmentRepository : JpaRepository<StudentEnrollment, UUID> {

    fun findBySchoolId(schoolId: UUID): List<StudentEnrollment>
    fun findByStudentId(studentId: UUID): List<StudentEnrollment>
    fun findBySectionId(sectionId: UUID): List<StudentEnrollment>
    fun findBySchoolIdAndSchoolYear(schoolId: UUID, schoolYear: String): List<StudentEnrollment>
    fun findBySchoolIdAndStatus(schoolId: UUID, status: StudentStatus): List<StudentEnrollment>

    // Check if student is already enrolled in the same school year
    fun existsByStudentIdAndSchoolYear(studentId: UUID, schoolYear: String): Boolean

    // Find active enrollment of a student — used for transfer
    fun findByStudentIdAndStatus(studentId: UUID, status: StudentStatus): StudentEnrollment?

    fun countBySectionIdAndStatus(sectionId: UUID, status: StudentStatus): Int


}