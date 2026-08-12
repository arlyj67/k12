package com.schoolproject.k12.service

import com.schoolproject.k12.dto.request.StudentEnrollmentRequest
import com.schoolproject.k12.dto.response.StudentEnrollmentResponse
import com.schoolproject.k12.model.StudentStatus
import java.util.UUID

interface StudentEnrollmentService {
    fun createEnrollment(request: StudentEnrollmentRequest): StudentEnrollmentResponse
    fun updateEnrollmentStatus(id: UUID, status: StudentStatus): StudentEnrollmentResponse
    fun transferStudent(enrollmentId: UUID, newSectionId: UUID): StudentEnrollmentResponse
    fun getEnrollmentById(id: UUID): StudentEnrollmentResponse
    fun getEnrollmentsBySchool(schoolId: UUID): List<StudentEnrollmentResponse>
    fun getEnrollmentsByStudent(studentId: UUID): List<StudentEnrollmentResponse>
    fun getEnrollmentsBySection(sectionId: UUID): List<StudentEnrollmentResponse>
    fun getEnrollmentsBySchoolYear(schoolId: UUID, schoolYear: String): List<StudentEnrollmentResponse>
    fun getEnrollmentsByStatus(schoolId: UUID, status: StudentStatus): List<StudentEnrollmentResponse>

}