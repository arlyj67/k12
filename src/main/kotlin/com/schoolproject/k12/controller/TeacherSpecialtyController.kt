package com.schoolproject.k12.controller

import com.schoolproject.k12.dto.request.TeacherSpecialtyRequest
import com.schoolproject.k12.dto.response.TeacherSpecialtyResponse
import com.schoolproject.k12.service.TeacherSpecialtyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/teacher-specialties")
class TeacherSpecialtyController(
    private val teacherSpecialtyService: TeacherSpecialtyService
) {

    @PostMapping
    fun addSpecialty(@RequestBody request: TeacherSpecialtyRequest): ResponseEntity<List<TeacherSpecialtyResponse>> =
        ResponseEntity.status(HttpStatus.CREATED).body(teacherSpecialtyService.addSpecialty(request))

    @GetMapping("/teacher/{employeeId}")
    fun getByTeacher(@PathVariable employeeId: UUID): ResponseEntity<List<TeacherSpecialtyResponse>> =
        ResponseEntity.ok(teacherSpecialtyService.getSpecialtiesByTeacher(employeeId))

    @GetMapping("/subject-area/{subjectAreaId}")
    fun getBySubjectArea(@PathVariable subjectAreaId: UUID): ResponseEntity<List<TeacherSpecialtyResponse>> =
        ResponseEntity.ok(teacherSpecialtyService.getTeachersBySubjectArea(subjectAreaId))

    @DeleteMapping("/teacher/{employeeId}/subject-area/{subjectAreaId}")
    fun removeSpecialty(
        @PathVariable employeeId: UUID,
        @PathVariable subjectAreaId: UUID
    ): ResponseEntity<Void> {
        teacherSpecialtyService.removeSpecialty(employeeId, subjectAreaId)
        return ResponseEntity.noContent().build()
    }
}