package com.schoolproject.k12.controller

import com.schoolproject.k12.dto.request.StudentEnrollmentRequest
import com.schoolproject.k12.dto.response.StudentEnrollmentResponse
import com.schoolproject.k12.model.StudentStatus
import com.schoolproject.k12.service.StudentEnrollmentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/enrollment")
class StudentEnrollmentController(
    private val enrollmentService: StudentEnrollmentService
) {

    @PostMapping
    fun createEnrollment(
        @Valid @RequestBody request: StudentEnrollmentRequest
    ): ResponseEntity<StudentEnrollmentResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(
            enrollmentService.createEnrollment(request)
        )

    @PatchMapping("/{id}/status")
    fun updateEnrollmentStatus(
        @PathVariable id: UUID,
        @RequestParam status: StudentStatus
    ): ResponseEntity<StudentEnrollmentResponse> =
        ResponseEntity.ok(enrollmentService.updateEnrollmentStatus(id, status))

    @PatchMapping("/{id}/transfer")
    fun transferStudent(
        @PathVariable id: UUID,
        @RequestParam newSectionId: UUID
    ): ResponseEntity<StudentEnrollmentResponse> =
        ResponseEntity.ok(enrollmentService.transferStudent(id, newSectionId))

    @GetMapping("/{id}")
    fun getEnrollmentById(
        @PathVariable id: UUID
    ): ResponseEntity<StudentEnrollmentResponse> =
        ResponseEntity.ok(enrollmentService.getEnrollmentById(id))

    @GetMapping("/school/{schoolId}")
    fun getEnrollmentsBySchool(
        @PathVariable schoolId: UUID
    ): ResponseEntity<List<StudentEnrollmentResponse>> =
        ResponseEntity.ok(enrollmentService.getEnrollmentsBySchool(schoolId))

    @GetMapping("/student/{studentId}")
    fun getEnrollmentsByStudent(
        @PathVariable studentId: UUID
    ): ResponseEntity<List<StudentEnrollmentResponse>> =
        ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId))

    @GetMapping("/section/{sectionId}")
    fun getEnrollmentsBySection(
        @PathVariable sectionId: UUID
    ): ResponseEntity<List<StudentEnrollmentResponse>> =
        ResponseEntity.ok(enrollmentService.getEnrollmentsBySection(sectionId))

    @GetMapping("/school/{schoolId}/school-year")
    fun getEnrollmentsBySchoolYear(
        @PathVariable schoolId: UUID,
        @RequestParam schoolYear: String
    ): ResponseEntity<List<StudentEnrollmentResponse>> =
        ResponseEntity.ok(enrollmentService.getEnrollmentsBySchoolYear(schoolId, schoolYear))

    @GetMapping("/school/{schoolId}/status")
    fun getEnrollmentsByStatus(
        @PathVariable schoolId: UUID,
        @RequestParam status: StudentStatus
    ): ResponseEntity<List<StudentEnrollmentResponse>> =
        ResponseEntity.ok(enrollmentService.getEnrollmentsByStatus(schoolId, status))
}