package com.schoolproject.k12.service

import com.schoolproject.k12.dto.request.GradeRequest
import com.schoolproject.k12.dto.response.GradeResponse
import com.schoolproject.k12.model.GradingPeriod
import java.util.UUID

interface GradeService {
    fun encodeGrade(request: GradeRequest): GradeResponse
    fun updateGrade(id: UUID, request: GradeRequest): GradeResponse
    fun getGradeById(id: UUID): GradeResponse
    fun getGradesByStudent(studentId: UUID): List<GradeResponse>
    fun getGradesByStudentAndSchoolYear(studentId: UUID, schoolYear: String): List<GradeResponse>
    fun getGradesByStudentAndGradingPeriod(studentId: UUID, gradingPeriod: GradingPeriod): List<GradeResponse>
    fun getGradesBySection(sectionId: UUID): List<GradeResponse>
    fun getGradesBySectionAndGradingPeriod(sectionId: UUID, gradingPeriod: GradingPeriod): List<GradeResponse>
    fun getGradesBySubjectAndSection(subjectId: UUID, sectionId: UUID): List<GradeResponse>
    fun getFinalGradesByStudent(studentId: UUID, schoolYear: String): Map<String, Double?>
    fun deleteGrade(id: UUID)
}