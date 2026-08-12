package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.request.StudentRequest
import com.schoolproject.k12.dto.response.StudentListResponse
import com.schoolproject.k12.dto.response.StudentResponse
import com.schoolproject.k12.entity.School
import com.schoolproject.k12.entity.Student
import com.schoolproject.k12.entity.User
import org.springframework.stereotype.Component

@Component
class StudentMapper {

    fun toEntity(dto: StudentRequest, user: User, school: School, studentNumber: String): Student {
        return Student(
            user = user,
            studentNumber = studentNumber,
            firstName = dto.firstName,
            middleName = dto.middleName,
            lastName = dto.lastName,
            dateOfBirth = dto.dateOfBirth,
            email = dto.email,
            gender = dto.gender,
            address = dto.address,
            contactNumber = dto.contactNumber,
            status = dto.status,
            studentLevel = dto.studentLevel,
            schoolYear = dto.schoolYear,
            guardianName = dto.guardianName,
            guardianContact = dto.guardianContact,
            guardianEmail = dto.guardianEmail,
            school = school,
        )
    }

    fun toResponse(student: Student): StudentResponse {
        return StudentResponse(
            id = student.id,
            studentNumber = student.studentNumber,
            fullName = student.fullName,
            dateOfBirth = student.dateOfBirth,
            gender = student.gender,
            address = student.address,
            email = student.email,
            contactNumber = student.contactNumber,
            status = student.status,
            studentLevel = student.studentLevel,
            schoolYear = student.schoolYear,
            guardianName = student.guardianName,
            guardianContact = student.guardianContact,
            guardianEmail = student.guardianContact,
            schoolName = student.school.schoolName,
            createdAt = student.createdAt,

        )
    }

    fun toListResponse(student: Student): StudentListResponse{
        return StudentListResponse(
            id = student.id,
            studentNumber = student.studentNumber,
            fullName = student.fullName,
        )
    }

    fun toListResponseList(students: List<Student>): List<StudentListResponse> =
        students.map { toListResponse(it) }
}