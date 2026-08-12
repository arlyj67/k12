package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.request.SectionRequest
import com.schoolproject.k12.dto.response.SectionResponse
import com.schoolproject.k12.entity.Employee
import com.schoolproject.k12.entity.Room
import com.schoolproject.k12.entity.School
import com.schoolproject.k12.entity.Section
import org.springframework.stereotype.Component

@Component
class SectionMapper {

    fun toEntity(dto: SectionRequest, room: Room, employee: Employee, school: School): Section {
        return Section(
            sectionName = dto.sectionName,
            studentLevel = dto.studentLevel,
            schoolYear = dto.schoolYear,
            room = room,
            adviserId = employee,
            school = school
        )
    }

    fun toResponse(section: Section): SectionResponse {
        return SectionResponse(
            id = section.id,
            sectionName = section.sectionName,
            studentLevel = section.studentLevel,
            schoolYear = section.schoolYear,
            roomNumber = section.room.roomNumber,
            adviserName = section.adviserId.fullName,
            createdAt = section.createdAt,
            schoolName = section.school.schoolName
        )
    }

    fun toResponseList(sections: List<Section>): List<SectionResponse> {
        return sections.map { toResponse(it) }
    }
}