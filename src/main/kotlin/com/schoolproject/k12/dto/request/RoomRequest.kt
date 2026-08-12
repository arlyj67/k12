package com.schoolproject.k12.dto.request

import java.util.UUID

data class RoomRequest (
    val schoolId: UUID,
    val roomNumber: String,
    val roomName: String? = null,
    val capacity: Int,
    val studentLevel: Int,
    val isAvailable: Boolean = true
)