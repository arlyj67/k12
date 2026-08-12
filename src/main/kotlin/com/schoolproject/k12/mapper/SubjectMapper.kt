package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.request.SubjectRequest
import com.schoolproject.k12.dto.response.SubjectResponse
import com.schoolproject.k12.entity.School
import com.schoolproject.k12.entity.Subject
import com.schoolproject.k12.entity.SubjectArea
import org.springframework.stereotype.Component
@Component
class SubjectMapper {

    fun toEntity(dto: SubjectRequest, school: School, subjectArea: SubjectArea): Subject {
        return Subject(
            school = school,
            subjectArea = subjectArea,
            subjectCode = dto.subjectCode,
            subjectName = dto.subjectName,
            description = dto.description,
            studentLevel = dto.studentLevel,
            schoolYear = dto.schoolYear,
        )
    }

    fun toResponse(subject: Subject): SubjectResponse {
        return SubjectResponse(
            id = subject.id,
            schoolId = subject.school.id!!,
            schoolName = subject.school.schoolName,
            subjectAreaId = subject.subjectArea.id,
            areaName = subject.subjectArea.areaName,
            subjectCode = subject.subjectCode,
            subjectName = subject.subjectName,
            description = subject.description,
            studentLevel = subject.studentLevel,
            schoolYear = subject.schoolYear,
            createdAt = subject.createdAt
        )
    }

    fun toResponseList(subjects: List<Subject>): List<SubjectResponse> {
        return subjects.map { toResponse(it) }
    }
}