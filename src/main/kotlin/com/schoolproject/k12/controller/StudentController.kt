package com.schoolproject.k12.controller

import com.schoolproject.k12.service.StudentService
import com.schoolproject.k12.dto.request.StudentRequest
import com.schoolproject.k12.dto.response.StudentListResponse
import com.schoolproject.k12.dto.response.StudentResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/students")
class StudentController(
    private val studentService: StudentService
) {

    // Register new student
    @PostMapping("/register")
    fun registerStudent(
        @RequestBody dto: StudentRequest
    ): ResponseEntity<StudentResponse> =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(studentService.registerStudent(dto))

    // Registrar activates student
    @PatchMapping("/{id}/activate")
    fun activateStudent(
        @PathVariable id: UUID
    ): ResponseEntity<StudentResponse> =
        ResponseEntity.ok(studentService.activateStudent(id))

    // Get all pending students (for registrar)
    @GetMapping("/pending/{schoolId}")
    fun getPendingStudents(
        @PathVariable schoolId: UUID
    ): ResponseEntity<List<StudentResponse>> =
        ResponseEntity.ok(studentService.getPendingStudents(schoolId))

    // Get all students by school
    @GetMapping("/school/{schoolId}")
    fun getStudentsBySchool(
        @PathVariable schoolId: UUID
    ): ResponseEntity<List<StudentResponse>> =
        ResponseEntity.ok(studentService.getStudentsBySchool(schoolId))

    @GetMapping("/school/{schoolId}/list")
    fun getStudentsListBySchool(
        @PathVariable schoolId: UUID
    ): ResponseEntity<List<StudentListResponse>> =
        ResponseEntity.ok(studentService.getStudentsListBySchool(schoolId))

    // Get student by id
    @GetMapping("/{id}")
    fun getStudentById(
        @PathVariable id: UUID
    ): ResponseEntity<StudentResponse> =
        ResponseEntity.ok(studentService.getStudentById(id))

    @GetMapping("/user/{userId}")
    fun getStudentByUserId(
        @PathVariable userId: UUID
    ): ResponseEntity<StudentResponse> =
        ResponseEntity.ok(studentService.getStudentByUserId(userId))

    // Get student by student number
    @GetMapping("/number/{studentNumber}")
    fun getStudentByNumber(
        @PathVariable studentNumber: String
    ): ResponseEntity<StudentResponse> =
        ResponseEntity.ok(studentService.getStudentByNumber(studentNumber))
}