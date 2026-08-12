package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.request.SchoolRequest
import com.schoolproject.k12.dto.response.SchoolResponse
import com.schoolproject.k12.entity.School
import org.springframework.stereotype.Component

@Component
class SchoolMapper {

    fun toEntity(dto: SchoolRequest): School {
        return School(
            schoolName = dto.schoolName,
            address = dto.address,
            contactNumber = dto.contactNumber,
            email = dto.email,
            principalName = dto.principalName,
            isActive = dto.isActive
        )
    }

    fun toResponse(school: School): SchoolResponse {
        return SchoolResponse(
            id = school.id,
            schoolName = school.schoolName,
            address = school.address,
            contactNumber = school.contactNumber,
            email = school.email,
            principalName = school.principalName,
            isActive = school.isActive,
            createdAt = school.createdAt
        )
    }
}