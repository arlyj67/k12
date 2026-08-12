package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.request.GradeRequest
import com.schoolproject.k12.dto.response.GradeResponse
import com.schoolproject.k12.entity.Employee
import com.schoolproject.k12.entity.Grade
import com.schoolproject.k12.entity.GradingCriteria
import com.schoolproject.k12.entity.School
import com.schoolproject.k12.entity.Student
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class GradeMapper {

    fun toEntity(
        dto: GradeRequest,
        school: School,
        student: Student,
        gradingCriteria: GradingCriteria,
        encodedBy: Employee,
        initialGrade: Double,
        transmutedGrade: Double
    ): Grade {
        return Grade(
            school = school,
            student = student,
            gradingCriteria = gradingCriteria,
            writtenWorksScore = dto.writtenWorksScore,
            performanceTaskScore = dto.performanceTaskScore,
            quarterlyAssessmentScore = dto.quarterlyAssessmentScore,
            initialGrade = initialGrade,
            transmutedGrade = transmutedGrade,
            encodedBy = encodedBy,
            encodedAt = LocalDateTime.now()
        )
    }

    fun toResponse(grade: Grade): GradeResponse {
        val criteria = grade.gradingCriteria
        return GradeResponse(
            id = grade.requireId(),
            schoolId = grade.school.requireId(),
            schoolName = grade.school.schoolName,
            studentId = grade.student.requireId(),
            studentNumber = grade.student.studentNumber,
            studentName = grade.student.fullName,
            subjectId = criteria.subject.requireId(),
            subjectName = criteria.subject.subjectName,
            subjectCode = criteria.subject.subjectCode,
            sectionId = criteria.section.requireId(),
            sectionName = criteria.section.sectionName,
            schoolYear = criteria.schoolYear,
            gradingPeriod = criteria.gradingPeriod,
            writtenWorksScore = grade.writtenWorksScore,
            writtenWorksTotal = criteria.writtenWorksTotal,
            performanceTaskScore = grade.performanceTaskScore,
            performanceTaskTotal = criteria.performanceTaskTotal,
            quarterlyAssessmentScore = grade.quarterlyAssessmentScore,
            quarterlyAssessmentTotal = criteria.quarterlyAssessmentTotal,
            initialGrade = grade.initialGrade,
            transmutedGrade = grade.transmutedGrade,
            encodedById = grade.encodedBy.id
                ?: error("Employee has not been persisted yet — id is null"),
            encodedByName = grade.encodedBy.fullName,
            encodedAt = grade.encodedAt,
            createdAt = grade.createdAt,
            updatedAt = grade.updatedAt
        )
    }

    fun toResponseList(grades: List<Grade>): List<GradeResponse> =
        grades.map { toResponse(it) }
}