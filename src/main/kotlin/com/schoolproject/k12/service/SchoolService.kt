package com.schoolproject.k12.service

import com.schoolproject.k12.dto.request.SchoolRequest
import com.schoolproject.k12.dto.response.SchoolResponse
import java.util.UUID

interface SchoolService {
    fun createSchool(dto: SchoolRequest): SchoolResponse
    fun getAllSchools(): List<SchoolResponse>
    fun getSchoolById(id: UUID): SchoolResponse
    fun getActiveSchools(): List<SchoolResponse>
    fun updateSchool(id: UUID, dto: SchoolRequest): SchoolResponse
    fun deleteSchool(id: UUID)
}