package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.request.RoomRequest
import com.schoolproject.k12.dto.response.RoomResponse
import com.schoolproject.k12.entity.Room
import com.schoolproject.k12.entity.School
import org.springframework.stereotype.Component

@Component
class RoomMapper {

    fun toEntity(dto: RoomRequest, school: School): Room {
        return Room(
            school = school,
            roomNumber = dto.roomNumber,
            roomName = dto.roomName,
            capacity = dto.capacity,
            studentLevel = dto.studentLevel,
            isAvailable = dto.isAvailable
        )
    }

    fun toResponse(room: Room): RoomResponse {
        return RoomResponse(
            id = room.id,
            schoolName = room.school.schoolName,
            roomNumber = room.roomNumber,
            roomName = room.roomName,
            capacity = room.capacity,
            studentLevel = room.studentLevel,
            isAvailable = room.isAvailable,
            createdAt = room.createdAt
        )
    }

    fun toResponseList(rooms: List<Room>): List<RoomResponse> {
        return rooms.map { toResponse(it) }
    }
}