package com.schoolproject.k12.dto.response

import com.schoolproject.k12.model.DayOfWeek
import com.schoolproject.k12.model.ScheduleStatus
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

data class TeacherScheduleResponse(
    val id: UUID,
    val employeeId: UUID,
    val employeeName: String,
    val sectionId: UUID,
    val sectionName: String,
    val subjectId: UUID,
    val subjectName: String,
    val subjectCode: String,
    val assignedById: UUID?,
    val assignedByName: String?,
    val dayOfWeek: DayOfWeek,
    val timeStart: LocalTime,
    val timeEnd: LocalTime,
    val schoolYear: String,
    val status: ScheduleStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)