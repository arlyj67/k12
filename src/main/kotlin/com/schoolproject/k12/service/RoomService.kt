package com.schoolproject.k12.service

import com.schoolproject.k12.dto.request.RoomRequest
import com.schoolproject.k12.dto.response.RoomResponse
import java.util.UUID

interface RoomService {
    fun createRoom(dto: RoomRequest): RoomResponse
    fun getAllBySchool(schoolId: UUID): List<RoomResponse>
    fun getAvailableBySchool(schoolId: UUID): List<RoomResponse>
    fun getBySchoolAndStudentLevel(schoolId: UUID, studentLevel: Int): List<RoomResponse>
    fun getById(id: UUID): RoomResponse
    fun updateRoom(id: UUID, dto: RoomRequest): RoomResponse
    fun deleteRoom(id: UUID)
}