package com.schoolproject.k12.dto.request

import java.util.UUID

data class TeacherSpecialtyRequest(
    val employeeId: UUID,
    val subjectAreaIds: List<UUID>
)
