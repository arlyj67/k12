package com.schoolproject.k12.service

import com.schoolproject.k12.dto.request.SectionRequest
import com.schoolproject.k12.dto.response.SectionResponse
import com.schoolproject.k12.entity.Employee
import java.util.UUID

interface SectionService {
    fun createSection(dto: SectionRequest): SectionResponse
    fun getAllBySchool(schoolId: UUID): List<SectionResponse>
    fun getBySchoolAndSchoolYear(schoolId: UUID, schoolYear: String): List<SectionResponse>
    fun getBySchoolAndStudentLevel(schoolId: UUID, studentLevel: Int): List<SectionResponse>
    fun getByAdviser(adviserId: UUID): List<SectionResponse>
    fun getById(id: UUID): SectionResponse
    fun updateSection(id: UUID, dto: SectionRequest): SectionResponse
    fun deleteSection(id: UUID)
}