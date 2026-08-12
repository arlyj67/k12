package com.schoolproject.k12.dto.response

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

data class ClassScheduleResponse(
    val id: UUID,
    val schoolName: String,
    val subjectName: String,
    val roomNumber: String,
    val teacherName: String,
    val sectionName: String,
    val dayOfWeek: DayOfWeek,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val schoolYear: String,
    val createdAt: LocalDateTime
)
