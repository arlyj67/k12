package com.schoolproject.k12.service

import com.schoolproject.k12.dto.request.TeacherScheduleRequest
import com.schoolproject.k12.dto.response.TeacherScheduleResponse
import com.schoolproject.k12.model.ScheduleStatus
import java.util.UUID

interface TeacherScheduleService {
    fun createSchedule(request: TeacherScheduleRequest): List<TeacherScheduleResponse>
    fun getSchedulesBySchool(schoolId: UUID): List<TeacherScheduleResponse>
    fun getSchedulesByTeacher(employeeId: UUID): List<TeacherScheduleResponse>
    fun getSchedulesBySection(sectionId: UUID): List<TeacherScheduleResponse>
    fun getSchedulesBySubject(subjectId: UUID): List<TeacherScheduleResponse>
    fun getSchedulesByTeacherAndSchoolYear(employeeId: UUID, schoolYear: String): List<TeacherScheduleResponse>
    fun getSchedulesBySectionAndSchoolYear(sectionId: UUID, schoolYear: String): List<TeacherScheduleResponse>
    fun updateSchedule(id: UUID, request: TeacherScheduleRequest): TeacherScheduleResponse
    fun updateScheduleStatus(id: UUID, status: ScheduleStatus): TeacherScheduleResponse
    fun deleteSchedule(id: UUID)
    fun getSchedulesByStudent(studentId: UUID): List<TeacherScheduleResponse>
}