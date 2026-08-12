package com.schoolproject.k12.controller

import com.schoolproject.k12.dto.request.SchoolRequest
import com.schoolproject.k12.dto.response.SchoolResponse
import com.schoolproject.k12.service.SchoolService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/schools")
class SchoolController(
    private val schoolService: SchoolService
) {

    @PostMapping
    fun createSchool(
        @RequestBody dto: SchoolRequest
    ): ResponseEntity<SchoolResponse> =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(schoolService.createSchool(dto))

    @GetMapping
    fun getAllSchools(): ResponseEntity<List<SchoolResponse>> =
        ResponseEntity.ok(schoolService.getAllSchools())

    @GetMapping("/{id}")
    fun getSchoolById(
        @PathVariable id: UUID
    ): ResponseEntity<SchoolResponse> =
        ResponseEntity.ok(schoolService.getSchoolById(id))

    @GetMapping("/active")
    fun getActiveSchools(): ResponseEntity<List<SchoolResponse>> =
        ResponseEntity.ok(schoolService.getActiveSchools())

    @PutMapping("/{id}")
    fun updateSchool(
        @PathVariable id: UUID,
        @RequestBody dto: SchoolRequest
    ): ResponseEntity<SchoolResponse> =
        ResponseEntity.ok(schoolService.updateSchool(id, dto))

    @DeleteMapping("/{id}")
    fun deleteSchool(
        @PathVariable id: UUID
    ): ResponseEntity<Void> {
        schoolService.deleteSchool(id)
        return ResponseEntity.noContent().build()
    }
}