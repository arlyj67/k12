package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.request.AttendanceRequest
import com.schoolproject.k12.dto.response.AttendanceResponse
import com.schoolproject.k12.entity.Attendance
import com.schoolproject.k12.entity.Employee
import com.schoolproject.k12.entity.School
import com.schoolproject.k12.entity.Section
import com.schoolproject.k12.entity.Student
import org.springframework.stereotype.Component

@Component
class AttendanceMapper {

    fun toEntity(
        dto: AttendanceRequest,
        student: Student,
        section: Section,
        school: School,
        recordedBy: Employee
    ): Attendance {
        return Attendance(
            student = student,
            section = section,
            school = school,
            recordedBy = recordedBy,
            date = dto.date,
            status = dto.status,
            remarks = dto.remarks
        )
    }

    fun toResponse(attendance: Attendance): AttendanceResponse {
        return AttendanceResponse(
            id = attendance.id,
            studentName = attendance.student.fullName,
            sectionName = attendance.section.sectionName,
            schoolName = attendance.school.schoolName,
            recordedBy = attendance.recordedBy.fullName,
            date = attendance.date,
            status = attendance.status,
            remarks = attendance.remarks,
            createdAt = attendance.createdAt
        )
    }
}