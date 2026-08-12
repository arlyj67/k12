package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.request.ClassScheduleRequest
import com.schoolproject.k12.dto.response.ClassScheduleResponse
import com.schoolproject.k12.entity.ClassSchedule
import com.schoolproject.k12.entity.Employee
import com.schoolproject.k12.entity.Room
import com.schoolproject.k12.entity.School
import com.schoolproject.k12.entity.Section
import com.schoolproject.k12.entity.Subject
import org.springframework.stereotype.Component

@Component
class ClassScheduleMapper {

    fun toEntity(
        dto: ClassScheduleRequest,
        subject: Subject,
        room: Room,
        teacher: Employee,
        section: Section,
        school: School
    ): ClassSchedule {
        return ClassSchedule(
            subject = subject,
            room = room,
            teacher = teacher,
            section = section,
            school = school,
            dayOfWeek = dto.dayOfWeek,
            startTime = dto.startTime,
            endTime = dto.endTime,
            schoolYear = dto.schoolYear
        )
    }

    fun toResponse(classSchedule: ClassSchedule): ClassScheduleResponse {
        return ClassScheduleResponse(
            id = classSchedule.id,
            subjectName = classSchedule.subject.subjectName,
            roomNumber = classSchedule.room.roomNumber,
            teacherName = classSchedule.teacher.fullName,
            sectionName = classSchedule.section.sectionName,
            schoolName = classSchedule.school.schoolName,
            dayOfWeek = classSchedule.dayOfWeek,
            startTime = classSchedule.startTime,
            endTime = classSchedule.endTime,
            schoolYear = classSchedule.schoolYear,
            createdAt = classSchedule.createdAt
        )
    }
}