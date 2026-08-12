package com.schoolproject.k12.controller

import com.schoolproject.k12.dto.request.SubjectRequest
import com.schoolproject.k12.dto.response.SubjectResponse
import com.schoolproject.k12.service.SubjectService
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
@RequestMapping("/api/subjects")
class SubjectsController(
    private val subjectService: SubjectService
) {

    @PostMapping("/add")
    fun create(@RequestBody dto: SubjectRequest): ResponseEntity<SubjectResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(subjectService.createSubject(dto))

    @GetMapping("/school/{schoolId}")
    fun getAllBySchool(@PathVariable schoolId: UUID): ResponseEntity<List<SubjectResponse>> =
        ResponseEntity.ok(subjectService.getAllBySchool(schoolId))

    @GetMapping("/school/{schoolId}/level")
    fun getBySchoolAndStudentLevel(
        @PathVariable schoolId: UUID,
        @RequestParam studentLevel: Int
    ): ResponseEntity<List<SubjectResponse>> =
        ResponseEntity.ok(subjectService.getBySchoolAndStudentLevel(schoolId, studentLevel))

    @GetMapping("/subject-area/{subjectAreaId}")
    fun getBySubjectArea(@PathVariable subjectAreaId: UUID): ResponseEntity<List<SubjectResponse>> =
        ResponseEntity.ok(subjectService.getBySubjectArea(subjectAreaId))

    @GetMapping("/code/{subjectCode}")
    fun getBySubjectCode(@PathVariable subjectCode: String): ResponseEntity<SubjectResponse> =
        ResponseEntity.ok(subjectService.getBySubjectCode(subjectCode))

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<SubjectResponse> =
        ResponseEntity.ok(subjectService.getById(id))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody dto: SubjectRequest
    ): ResponseEntity<SubjectResponse> =
        ResponseEntity.ok(subjectService.updateSubject(id, dto))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        subjectService.deleteSubject(id)
        return ResponseEntity.noContent().build()
    }
}