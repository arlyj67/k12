package com.schoolproject.k12.controller

import com.schoolproject.k12.dto.request.SectionRequest
import com.schoolproject.k12.dto.response.SectionResponse
import com.schoolproject.k12.service.SectionService
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
@RequestMapping("/api/sections")
class SectionController(
    private val sectionService: SectionService
) {

    @PostMapping
    fun create(@RequestBody dto: SectionRequest): ResponseEntity<SectionResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(sectionService.createSection(dto))

    @GetMapping("/school/{schoolId}")
    fun getAllBySchool(@PathVariable schoolId: UUID): ResponseEntity<List<SectionResponse>> =
        ResponseEntity.ok(sectionService.getAllBySchool(schoolId))

    @GetMapping("/school/{schoolId}/school-year")
    fun getBySchoolAndSchoolYear(
        @PathVariable schoolId: UUID,
        @RequestParam schoolYear: String
    ): ResponseEntity<List<SectionResponse>> =
        ResponseEntity.ok(sectionService.getBySchoolAndSchoolYear(schoolId, schoolYear))

    @GetMapping("/school/{schoolId}/level")
    fun getBySchoolAndStudentLevel(
        @PathVariable schoolId: UUID,
        @RequestParam studentLevel: Int
    ): ResponseEntity<List<SectionResponse>> =
        ResponseEntity.ok(sectionService.getBySchoolAndStudentLevel(schoolId, studentLevel))

    @GetMapping("/adviser/{adviserId}")
    fun getByAdviser(@PathVariable adviserId: UUID): ResponseEntity<List<SectionResponse>> =
        ResponseEntity.ok(sectionService.getByAdviser(adviserId))

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<SectionResponse> =
        ResponseEntity.ok(sectionService.getById(id))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody dto: SectionRequest
    ): ResponseEntity<SectionResponse> =
        ResponseEntity.ok(sectionService.updateSection(id, dto))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        sectionService.deleteSection(id)
        return ResponseEntity.noContent().build()
    }
}