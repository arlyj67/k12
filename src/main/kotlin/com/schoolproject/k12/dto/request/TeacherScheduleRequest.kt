package com.schoolproject.k12.dto.request

import com.schoolproject.k12.model.DayOfWeek
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.time.LocalTime
import java.util.UUID

data class TeacherScheduleRequest(
    val employeeId: UUID,
    val sectionId: UUID,
    val subjectId: UUID,
    val assignedById: UUID? = null,

    @field:NotEmpty(message = "At least one day must be provided")
    val days: List<DayOfWeek>,

    val timeStart: LocalTime,
    val timeEnd: LocalTime,

    @field:NotBlank(message = "School year must not be blank")
    val schoolYear: String
) {
    init {
        require(timeEnd.isAfter(timeStart)) {
            "timeEnd ($timeEnd) must be after timeStart ($timeStart)"
        }
    }
}