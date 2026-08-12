package com.schoolproject.k12.controller

import com.schoolproject.k12.dto.request.RoomRequest
import com.schoolproject.k12.dto.response.RoomResponse
import com.schoolproject.k12.service.RoomService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/rooms")
class RoomController(
    private val roomService: RoomService
) {

    @PostMapping
    fun create(@RequestBody dto: RoomRequest): ResponseEntity<RoomResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(dto))

    @GetMapping("/school/{schoolId}")
    fun getAllBySchool(@PathVariable schoolId: UUID): ResponseEntity<List<RoomResponse>> =
        ResponseEntity.ok(roomService.getAllBySchool(schoolId))

    @GetMapping("/school/{schoolId}/available")
    fun getAvailableBySchool(@PathVariable schoolId: UUID): ResponseEntity<List<RoomResponse>> =
        ResponseEntity.ok(roomService.getAvailableBySchool(schoolId))

    @GetMapping("/school/{schoolId}/level")
    fun getBySchoolAndStudentLevel(
        @PathVariable schoolId: UUID,
        @RequestParam studentLevel: Int
    ): ResponseEntity<List<RoomResponse>> =
        ResponseEntity.ok(roomService.getBySchoolAndStudentLevel(schoolId, studentLevel))

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<RoomResponse> =
        ResponseEntity.ok(roomService.getById(id))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody dto: RoomRequest
    ): ResponseEntity<RoomResponse> =
        ResponseEntity.ok(roomService.updateRoom(id, dto))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        roomService.deleteRoom(id)
        return ResponseEntity.noContent().build()
    }
}