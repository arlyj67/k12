package com.schoolproject.k12.dto.request

import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

data class ClassScheduleRequest(
    val schoolId: UUID,
    val subjectId: UUID,
    val roomId: UUID,
    val teacherId: UUID,
    val sectionId: UUID,
    val dayOfWeek: DayOfWeek,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val schoolYear: String
)
