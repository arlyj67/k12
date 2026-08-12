package com.schoolproject.k12.dto.response

import java.time.LocalDateTime
import java.util.UUID

data class RoomResponse (
    val id: UUID?,
    val schoolName: String,
    val roomNumber: String,
    val roomName: String?,
    val capacity: Int,
    val studentLevel: Int,
    val isAvailable: Boolean,
    val createdAt: LocalDateTime
)