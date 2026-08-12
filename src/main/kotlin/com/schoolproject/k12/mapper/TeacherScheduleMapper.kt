package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.response.TeacherScheduleResponse
import com.schoolproject.k12.entity.TeacherSchedule
import org.springframework.stereotype.Component

@Component
class TeacherScheduleMapper {

    fun toResponse(schedule: TeacherSchedule): TeacherScheduleResponse {
        return TeacherScheduleResponse(
            id = schedule.requireId(),
            employeeId = schedule.employee.id ?: error("Employee has not been persisted yet — id is null"),
            employeeName = schedule.employee.fullName,
            sectionId = schedule.section.requireId(),
            sectionName = schedule.section.sectionName,
            subjectId = schedule.subject.requireId(),
            subjectName = schedule.subject.subjectName,
            subjectCode = schedule.subject.subjectCode,
            assignedById = schedule.assignedBy?.requireId(),
            assignedByName = schedule.assignedBy?.username,
            dayOfWeek = schedule.dayOfWeek,
            timeStart = schedule.timeStart,
            timeEnd = schedule.timeEnd,
            schoolYear = schedule.schoolYear,
            status = schedule.status,
            createdAt = schedule.createdAt,
            updatedAt = schedule.updatedAt
        )
    }

    fun toResponseList(schedules: List<TeacherSchedule>): List<TeacherScheduleResponse> =
        schedules.map { toResponse(it) }
}