package com.schoolproject.k12.controller

import com.schoolproject.k12.dto.request.GradingCriteriaRequest
import com.schoolproject.k12.dto.response.GradingCriteriaResponse
import com.schoolproject.k12.model.GradingPeriod
import com.schoolproject.k12.service.GradingCriteriaService
import jakarta.validation.Valid
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
@RequestMapping("/api/grading-criteria")
class GradingCriteriaController(
    private val gradingCriteriaService: GradingCriteriaService
) {

    @PostMapping
    fun createCriteria(
        @Valid @RequestBody request: GradingCriteriaRequest
    ): ResponseEntity<GradingCriteriaResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(
            gradingCriteriaService.createCriteria(request)
        )

    @PutMapping("/{id}")
    fun updateCriteria(
        @PathVariable id: UUID,
        @Valid @RequestBody request: GradingCriteriaRequest
    ): ResponseEntity<GradingCriteriaResponse> =
        ResponseEntity.ok(gradingCriteriaService.updateCriteria(id, request))

    @GetMapping("/{id}")
    fun getCriteriaById(
        @PathVariable id: UUID
    ): ResponseEntity<GradingCriteriaResponse> =
        ResponseEntity.ok(gradingCriteriaService.getCriteriaById(id))

    @GetMapping("/section/{sectionId}")
    fun getCriteriaBySection(
        @PathVariable sectionId: UUID
    ): ResponseEntity<List<GradingCriteriaResponse>> =
        ResponseEntity.ok(gradingCriteriaService.getCriteriaBySection(sectionId))

    @GetMapping("/subject/{subjectId}")
    fun getCriteriaBySubject(
        @PathVariable subjectId: UUID
    ): ResponseEntity<List<GradingCriteriaResponse>> =
        ResponseEntity.ok(gradingCriteriaService.getCriteriaBySubject(subjectId))

    @GetMapping("/section/{sectionId}/school-year")
    fun getCriteriaBySectionAndSchoolYear(
        @PathVariable sectionId: UUID,
        @RequestParam schoolYear: String
    ): ResponseEntity<List<GradingCriteriaResponse>> =
        ResponseEntity.ok(
            gradingCriteriaService.getCriteriaBySectionAndSchoolYear(sectionId, schoolYear)
        )

    @GetMapping("/section/{sectionId}/grading-period")
    fun getCriteriaBySectionAndGradingPeriod(
        @PathVariable sectionId: UUID,
        @RequestParam gradingPeriod: GradingPeriod
    ): ResponseEntity<List<GradingCriteriaResponse>> =
        ResponseEntity.ok(
            gradingCriteriaService.getCriteriaBySectionAndGradingPeriod(sectionId, gradingPeriod)
        )

    @DeleteMapping("/{id}")
    fun deleteCriteria(
        @PathVariable id: UUID
    ): ResponseEntity<Unit> {
        gradingCriteriaService.deleteCriteria(id)
        return ResponseEntity.noContent().build()
    }
}