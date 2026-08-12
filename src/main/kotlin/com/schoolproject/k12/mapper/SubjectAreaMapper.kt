package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.request.SubjectAreaRequest
import com.schoolproject.k12.dto.response.SubjectAreaResponse
import com.schoolproject.k12.entity.School
import com.schoolproject.k12.entity.SubjectArea
import org.springframework.stereotype.Component

@Component
class SubjectAreaMapper {

    fun toEntity(dto: SubjectAreaRequest, school: School): SubjectArea {
        return SubjectArea(
            school = school,
            areaName = dto.areaName,
            description = dto.description,
            isActive = dto.isActive
        )
    }

    fun toResponse(subjectArea: SubjectArea): SubjectAreaResponse {
        return SubjectAreaResponse(
            id = subjectArea.id,
            schoolId = subjectArea.school.id!!,
            areaName = subjectArea.areaName,
            description = subjectArea.description,
            isActive = subjectArea.isActive
        )
    }

    fun toResponseList(subjectAreas: List<SubjectArea>): List<SubjectAreaResponse> {
        return subjectAreas.map { toResponse(it) }
    }
}