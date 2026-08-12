package com.schoolproject.k12.dto.request

import com.schoolproject.k12.model.AttendanceStatus
import java.time.LocalDate
import java.util.UUID

data class AttendanceRequest(
    val studentId: UUID,
    val sectionId: UUID,
    val schoolId: UUID,
    val recordedById: UUID,
    val date: LocalDate,
    val status: AttendanceStatus,
    val remarks: String? = null
)
