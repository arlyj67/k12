package com.schoolproject.k12.service

import com.schoolproject.k12.dto.request.TeacherSpecialtyRequest
import com.schoolproject.k12.dto.response.TeacherSpecialtyResponse
import java.util.UUID

interface TeacherSpecialtyService {
    fun addSpecialty(request: TeacherSpecialtyRequest): List<TeacherSpecialtyResponse>
    fun getSpecialtiesByTeacher(employeeId: UUID): List<TeacherSpecialtyResponse>
    fun getTeachersBySubjectArea(subjectAreaId: UUID): List<TeacherSpecialtyResponse>
    fun removeSpecialty(employeeId: UUID, subjectAreaId: UUID)
}