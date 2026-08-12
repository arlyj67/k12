package com.schoolproject.k12.controller

import com.schoolproject.k12.dto.request.TeacherScheduleRequest
import com.schoolproject.k12.dto.response.TeacherScheduleResponse
import com.schoolproject.k12.model.ScheduleStatus
import com.schoolproject.k12.service.TeacherScheduleService
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
@RequestMapping("/api/teacher-schedules")
class TeacherScheduleController(
    private val teacherScheduleService: TeacherScheduleService
) {

    @PostMapping
    fun createSchedule(@RequestBody request: TeacherScheduleRequest): ResponseEntity<List<TeacherScheduleResponse>> =
        ResponseEntity.status(HttpStatus.CREATED).body(teacherScheduleService.createSchedule(request))

    @GetMapping("/teacher/{employeeId}")
    fun getByTeacher(@PathVariable employeeId: UUID): ResponseEntity<List<TeacherScheduleResponse>> =
        ResponseEntity.ok(teacherScheduleService.getSchedulesByTeacher(employeeId))

    @GetMapping("/section/{sectionId}")
    fun getBySection(@PathVariable sectionId: UUID): ResponseEntity<List<TeacherScheduleResponse>> =
        ResponseEntity.ok(teacherScheduleService.getSchedulesBySection(sectionId))

    @GetMapping("/subject/{subjectId}")
    fun getBySubject(@PathVariable subjectId: UUID): ResponseEntity<List<TeacherScheduleResponse>> =
        ResponseEntity.ok(teacherScheduleService.getSchedulesBySubject(subjectId))

    @GetMapping("/teacher/{employeeId}/school-year")
    fun getByTeacherAndSchoolYear(
        @PathVariable employeeId: UUID,
        @RequestParam schoolYear: String
    ): ResponseEntity<List<TeacherScheduleResponse>> =
        ResponseEntity.ok(teacherScheduleService.getSchedulesByTeacherAndSchoolYear(employeeId, schoolYear))

    @GetMapping("/section/{sectionId}/school-year")
    fun getBySectionAndSchoolYear(
        @PathVariable sectionId: UUID,
        @RequestParam schoolYear: String
    ): ResponseEntity<List<TeacherScheduleResponse>> =
        ResponseEntity.ok(teacherScheduleService.getSchedulesBySectionAndSchoolYear(sectionId, schoolYear))

    @PutMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: UUID,
        @RequestParam status: ScheduleStatus
    ): ResponseEntity<TeacherScheduleResponse> =
        ResponseEntity.ok(teacherScheduleService.updateScheduleStatus(id, status))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        teacherScheduleService.deleteSchedule(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/student/{studentId}")
    fun getSchedulesByStudent(
        @PathVariable studentId: UUID
    ): ResponseEntity<List<TeacherScheduleResponse>> =
        ResponseEntity.ok(teacherScheduleService.getSchedulesByStudent(studentId))
}