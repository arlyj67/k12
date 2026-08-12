package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.response.TeacherSpecialtyResponse
import com.schoolproject.k12.entity.TeacherSpecialty
import org.springframework.stereotype.Component

@Component
class TeacherSpecialtyMapper {

    fun toResponse(specialty: TeacherSpecialty): TeacherSpecialtyResponse {
        return TeacherSpecialtyResponse(
            id = specialty.id,
            employeeId = specialty.employee.id,
            employeeName = specialty.employee.fullName,
            subjectAreaId = specialty.subjectArea.id,
            areaName = specialty.subjectArea.areaName
        )
    }

    fun toResponseList(specialties: List<TeacherSpecialty>): List<TeacherSpecialtyResponse> {
        return specialties.map { toResponse(it) }
    }
}