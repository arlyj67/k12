package com.schoolproject.k12.service

import com.schoolproject.k12.dto.request.SubjectAreaRequest
import com.schoolproject.k12.dto.response.SubjectAreaResponse
import java.util.UUID

interface SubjectAreaService {
    fun createSubjectArea(dto: SubjectAreaRequest): SubjectAreaResponse
    fun getAllBySchool(schoolId: UUID): List<SubjectAreaResponse>
    fun getActiveBySchool(schoolId: UUID): List<SubjectAreaResponse>
    fun getById(id: UUID): SubjectAreaResponse
    fun updateSubjectArea(id: UUID, dto: SubjectAreaRequest): SubjectAreaResponse
    fun deleteSubjectArea(id: UUID)
}