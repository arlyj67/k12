package com.schoolproject.k12.controller

import com.schoolproject.k12.dto.request.SubjectAreaRequest
import com.schoolproject.k12.dto.response.SubjectAreaResponse
import com.schoolproject.k12.service.SubjectAreaService
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
@RequestMapping("/api/subject-areas")
class SubjectAreaController(
    private val subjectAreaService: SubjectAreaService
) {

    @PostMapping("add")
    fun create(@RequestBody dto: SubjectAreaRequest): ResponseEntity<SubjectAreaResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(subjectAreaService.createSubjectArea(dto))

    @GetMapping("/school/{schoolId}")
    fun getAllBySchool(@PathVariable schoolId: UUID): ResponseEntity<List<SubjectAreaResponse>> =
        ResponseEntity.ok(subjectAreaService.getAllBySchool(schoolId))

    @GetMapping("/school/{schoolId}/active")
    fun getActiveBySchool(@PathVariable schoolId: UUID): ResponseEntity<List<SubjectAreaResponse>> =
        ResponseEntity.ok(subjectAreaService.getActiveBySchool(schoolId))

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<SubjectAreaResponse> =
        ResponseEntity.ok(subjectAreaService.getById(id))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody dto: SubjectAreaRequest
    ): ResponseEntity<SubjectAreaResponse> =
        ResponseEntity.ok(subjectAreaService.updateSubjectArea(id, dto))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        subjectAreaService.deleteSubjectArea(id)
        return ResponseEntity.noContent().build()
    }
}