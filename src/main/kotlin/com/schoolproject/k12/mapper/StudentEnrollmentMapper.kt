package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.request.StudentEnrollmentRequest
import com.schoolproject.k12.dto.response.StudentEnrollmentResponse
import com.schoolproject.k12.entity.School
import com.schoolproject.k12.entity.Section
import com.schoolproject.k12.entity.Student
import com.schoolproject.k12.entity.StudentEnrollment
import org.springframework.stereotype.Component

@Component
class StudentEnrollmentMapper {

    fun toEntity(
        dto: StudentEnrollmentRequest,
        student: Student,
        section: Section,
        school: School
    ): StudentEnrollment {
        return StudentEnrollment(
            student = student,
            section = section,
            school = school,
            schoolYear = dto.schoolYear,
            enrollmentDate = dto.enrollmentDate,
            status = dto.status
        )
    }

    fun toResponse(enrollment: StudentEnrollment): StudentEnrollmentResponse {
        return StudentEnrollmentResponse(
            id = enrollment.requireId(),
            schoolId = enrollment.school.requireId(),
            schoolName = enrollment.school.schoolName,
            studentId = enrollment.student.requireId(),
            studentNumber = enrollment.student.studentNumber,
            studentName = enrollment.student.fullName,
            sectionId = enrollment.section.requireId(),
            sectionName = enrollment.section.sectionName,
            schoolYear = enrollment.schoolYear,
            enrollmentDate = enrollment.enrollmentDate,
            status = enrollment.status,
            createdAt = enrollment.createdAt,
            updatedAt = enrollment.updatedAt
        )
    }

    fun toResponseList(enrollments: List<StudentEnrollment>): List<StudentEnrollmentResponse> =
        enrollments.map { toResponse(it) }
}