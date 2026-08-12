package com.schoolproject.k12.controller

import com.schoolproject.k12.dto.request.GradeRequest
import com.schoolproject.k12.dto.response.GradeResponse
import com.schoolproject.k12.model.GradingPeriod
import com.schoolproject.k12.service.GradeService
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
@RequestMapping("/api/grades")
class GradeController(
    private val gradeService: GradeService
) {

    @PostMapping
    fun encodeGrade(
        @Valid @RequestBody request: GradeRequest
    ): ResponseEntity<GradeResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(
            gradeService.encodeGrade(request)
        )

    @PutMapping("/{id}")
    fun updateGrade(
        @PathVariable id: UUID,
        @Valid @RequestBody request: GradeRequest
    ): ResponseEntity<GradeResponse> =
        ResponseEntity.ok(gradeService.updateGrade(id, request))

    @GetMapping("/{id}")
    fun getGradeById(
        @PathVariable id: UUID
    ): ResponseEntity<GradeResponse> =
        ResponseEntity.ok(gradeService.getGradeById(id))

    @GetMapping("/student/{studentId}")
    fun getGradesByStudent(
        @PathVariable studentId: UUID
    ): ResponseEntity<List<GradeResponse>> =
        ResponseEntity.ok(gradeService.getGradesByStudent(studentId))

    @GetMapping("/student/{studentId}/school-year")
    fun getGradesByStudentAndSchoolYear(
        @PathVariable studentId: UUID,
        @RequestParam schoolYear: String
    ): ResponseEntity<List<GradeResponse>> =
        ResponseEntity.ok(gradeService.getGradesByStudentAndSchoolYear(studentId, schoolYear))

    @GetMapping("/student/{studentId}/grading-period")
    fun getGradesByStudentAndGradingPeriod(
        @PathVariable studentId: UUID,
        @RequestParam gradingPeriod: GradingPeriod
    ): ResponseEntity<List<GradeResponse>> =
        ResponseEntity.ok(gradeService.getGradesByStudentAndGradingPeriod(studentId, gradingPeriod))

    @GetMapping("/section/{sectionId}")
    fun getGradesBySection(
        @PathVariable sectionId: UUID
    ): ResponseEntity<List<GradeResponse>> =
        ResponseEntity.ok(gradeService.getGradesBySection(sectionId))

    @GetMapping("/section/{sectionId}/grading-period")
    fun getGradesBySectionAndGradingPeriod(
        @PathVariable sectionId: UUID,
        @RequestParam gradingPeriod: GradingPeriod
    ): ResponseEntity<List<GradeResponse>> =
        ResponseEntity.ok(gradeService.getGradesBySectionAndGradingPeriod(sectionId, gradingPeriod))

    @GetMapping("/subject/{subjectId}/section/{sectionId}")
    fun getGradesBySubjectAndSection(
        @PathVariable subjectId: UUID,
        @PathVariable sectionId: UUID
    ): ResponseEntity<List<GradeResponse>> =
        ResponseEntity.ok(gradeService.getGradesBySubjectAndSection(subjectId, sectionId))

    @GetMapping("/student/{studentId}/final")
    fun getFinalGradesByStudent(
        @PathVariable studentId: UUID,
        @RequestParam schoolYear: String
    ): ResponseEntity<Map<String, Double?>> =
        ResponseEntity.ok(gradeService.getFinalGradesByStudent(studentId, schoolYear))

    @DeleteMapping("/{id}")
    fun deleteGrade(
        @PathVariable id: UUID
    ): ResponseEntity<Unit> {
        gradeService.deleteGrade(id)
        return ResponseEntity.noContent().build()
    }
}