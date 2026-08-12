package com.schoolproject.k12.repository

import com.schoolproject.k12.entity.Room
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RoomRepository : JpaRepository<Room, UUID> {
    fun findBySchoolId(schoolId: UUID): List<Room>
    fun findBySchoolIdAndIsAvailable(schoolId: UUID, isAvailable: Boolean): List<Room>
    fun findBySchoolIdAndStudentLevel(schoolId: UUID, studentLevel: Int): List<Room>
    fun findByRoomNumber(roomNumber: String): Room?
    fun existsByRoomNumber(roomNumber: String): Boolean
    fun existsBySchoolIdAndRoomNumber(schoolId: UUID, roomNumber: String): Boolean
}