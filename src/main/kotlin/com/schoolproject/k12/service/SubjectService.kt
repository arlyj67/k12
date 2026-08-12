package com.schoolproject.k12.service

import com.schoolproject.k12.dto.request.SubjectRequest
import com.schoolproject.k12.dto.response.SubjectResponse
import java.util.UUID

interface SubjectService {
    fun createSubject(dto: SubjectRequest): SubjectResponse
    fun getAllBySchool(schoolId: UUID): List<SubjectResponse>
    fun getBySchoolAndStudentLevel(schoolId: UUID, studentLevel: Int): List<SubjectResponse>
    fun getBySubjectArea(subjectAreaId: UUID): List<SubjectResponse>
    fun getBySubjectCode(subjectCode: String): SubjectResponse
    fun getById(id: UUID): SubjectResponse
    fun updateSubject(id: UUID, dto: SubjectRequest): SubjectResponse
    fun deleteSubject(id: UUID)
}