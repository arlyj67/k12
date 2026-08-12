package com.schoolproject.k12.dto.response

import java.util.UUID

data class TeacherSpecialtyResponse (
    val id: UUID,
    val employeeId: UUID?,
    val employeeName: String,
    val subjectAreaId: UUID?,
    val areaName: String
)