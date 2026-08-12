package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.dto.request.RoomRequest
import com.schoolproject.k12.dto.response.RoomResponse
import com.schoolproject.k12.mapper.RoomMapper
import com.schoolproject.k12.repository.RoomRepository
import com.schoolproject.k12.repository.SchoolRepository
import com.schoolproject.k12.service.RoomService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class RoomServiceImpl(
    private val roomRepository: RoomRepository,
    private val schoolRepository: SchoolRepository,
    private val roomMapper: RoomMapper
) : RoomService {

    override fun createRoom(dto: RoomRequest): RoomResponse {
        val school = schoolRepository.findById(dto.schoolId)
            .orElseThrow { NoSuchElementException("School not found") }

        if (roomRepository.existsBySchoolIdAndRoomNumber(dto.schoolId, dto.roomNumber)) {
            throw IllegalArgumentException("Room number already exists: ${dto.roomNumber}")
        }

        val room = roomRepository.save(roomMapper.toEntity(dto, school))
        return roomMapper.toResponse(room)
    }

    override fun getAllBySchool(schoolId: UUID): List<RoomResponse> =
        roomMapper.toResponseList(roomRepository.findBySchoolId(schoolId))

    override fun getAvailableBySchool(schoolId: UUID): List<RoomResponse> =
        roomMapper.toResponseList(roomRepository.findBySchoolIdAndIsAvailable(schoolId, true))

    override fun getBySchoolAndStudentLevel(schoolId: UUID, studentLevel: Int): List<RoomResponse> =
        roomMapper.toResponseList(roomRepository.findBySchoolIdAndStudentLevel(schoolId, studentLevel))

    override fun getById(id: UUID): RoomResponse =
        roomMapper.toResponse(
            roomRepository.findById(id)
                .orElseThrow { NoSuchElementException("Room not found") }
        )

    override fun updateRoom(id: UUID, dto: RoomRequest): RoomResponse {
        val room = roomRepository.findById(id)
            .orElseThrow { NoSuchElementException("Room not found") }

        room.roomName = dto.roomName
        room.capacity = dto.capacity
        room.studentLevel = dto.studentLevel
        room.isAvailable = dto.isAvailable
        room.updatedAt = LocalDateTime.now()

        return roomMapper.toResponse(roomRepository.save(room))
    }

    override fun deleteRoom(id: UUID) {
        if (!roomRepository.existsById(id)) {
            throw NoSuchElementException("Room not found")
        }
        roomRepository.deleteById(id)
    }
}