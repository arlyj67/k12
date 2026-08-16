package com.schoolproject.k12.service

import com.schoolproject.k12.dto.request.StudentRequest
import com.schoolproject.k12.dto.response.StudentListResponse
import com.schoolproject.k12.dto.response.StudentPendingResponse
import com.schoolproject.k12.dto.response.StudentResponse
import java.util.UUID

interface StudentService {

    fun generateStudentNumber(schoolId: UUID, gradeLevel: Int): String

    fun registerStudent(dto: StudentRequest): StudentResponse

    fun activateStudent(studentId: UUID): StudentResponse

    fun getStudentsBySchool(schoolId: UUID): List<StudentResponse>

    fun getStudentsListBySchool(schoolId: UUID): List<StudentListResponse>

    fun getStudentById(id: UUID): StudentResponse

    fun getStudentByNumber(studentNumber: String): StudentResponse

    fun getStudentByUserId(userId: UUID): StudentResponse

    fun getPendingStudents(schoolId: UUID): List<StudentPendingResponse>
}