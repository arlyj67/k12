package com.schoolproject.k12.dto.response

import com.schoolproject.k12.model.AttendanceStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class AttendanceResponse (
    val id: UUID,
    val studentName: String,
    val sectionName: String,
    val schoolName: String,
    val recordedBy: String,
    val date: LocalDate,
    val status: AttendanceStatus,
    val remarks: String?,
    val createdAt: LocalDateTime
)