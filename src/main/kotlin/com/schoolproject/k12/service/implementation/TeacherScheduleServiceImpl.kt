package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.dto.request.TeacherScheduleRequest
import com.schoolproject.k12.dto.response.TeacherScheduleResponse
import com.schoolproject.k12.entity.TeacherSchedule
import com.schoolproject.k12.mapper.TeacherScheduleMapper
import com.schoolproject.k12.model.Role
import com.schoolproject.k12.model.ScheduleStatus
import com.schoolproject.k12.model.StudentStatus
import com.schoolproject.k12.repository.EmployeeRepository
import com.schoolproject.k12.repository.SectionRepository
import com.schoolproject.k12.repository.StudentEnrollmentRepository
import com.schoolproject.k12.repository.SubjectRepository
import com.schoolproject.k12.repository.TeacherScheduleRepository
import com.schoolproject.k12.repository.UserRepository
import com.schoolproject.k12.service.TeacherScheduleService
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class TeacherScheduleServiceImpl(
    private val teacherScheduleRepository: TeacherScheduleRepository,
    private val employeeRepository: EmployeeRepository,
    private val sectionRepository: SectionRepository,
    private val subjectRepository: SubjectRepository,
    private val userRepository: UserRepository,
    private val studentEnrollmentRepository: StudentEnrollmentRepository,
    private val teacherScheduleMapper: TeacherScheduleMapper
) : TeacherScheduleService {

    override fun createSchedule(request: TeacherScheduleRequest): List<TeacherScheduleResponse> {
        val employee = employeeRepository.findById(request.employeeId)
            .orElseThrow { EntityNotFoundException("Employee not found with id: ${request.employeeId}") }

        // Compare against the Role enum constant directly — not a fragile string comparison
        if (employee.role != Role.TEACHER) {
            throw IllegalArgumentException("Employee ${request.employeeId} is not a teacher")
        }

        val section = sectionRepository.findById(request.sectionId)
            .orElseThrow { EntityNotFoundException("Section not found with id: ${request.sectionId}") }

        val subject = subjectRepository.findById(request.subjectId)
            .orElseThrow { EntityNotFoundException("Subject not found with id: ${request.subjectId}") }

        val assignedBy = request.assignedById?.let {
            userRepository.findById(it)
                .orElseThrow { EntityNotFoundException("User not found with id: $it") }
        }

        // timeEnd > timeStart is already enforced by TeacherScheduleRequest.init — no duplicate check needed here

        val schedules = request.days.map { day ->
            if (teacherScheduleRepository.hasConflict(
                    request.employeeId, day, request.timeStart, request.timeEnd
                )
            ) {
                throw IllegalArgumentException(
                    "Teacher has a conflicting schedule on ${day.name} at ${request.timeStart} - ${request.timeEnd}"
                )
            }

            if (teacherScheduleRepository.hasSectionConflict(
                    request.sectionId, day, request.timeStart, request.timeEnd
                )
            ) {
                throw IllegalArgumentException(
                    "Section has a conflicting schedule on ${day.name} at ${request.timeStart} - ${request.timeEnd}"
                )
            }

            teacherScheduleRepository.save(
                TeacherSchedule(
                    employee = employee,
                    section = section,
                    subject = subject,
                    assignedBy = assignedBy,
                    dayOfWeek = day,
                    timeStart = request.timeStart,
                    timeEnd = request.timeEnd,
                    schoolYear = request.schoolYear
                )
            )
        }

        return teacherScheduleMapper.toResponseList(schedules)
    }

    override fun getSchedulesBySchool(schoolId: UUID): List<TeacherScheduleResponse> =
        teacherScheduleMapper.toResponseList(
            teacherScheduleRepository.findByEmployeeSchoolId(schoolId)
        )

    override fun getSchedulesByTeacher(employeeId: UUID): List<TeacherScheduleResponse> =
        teacherScheduleMapper.toResponseList(
            teacherScheduleRepository.findByEmployeeId(employeeId)
        )

    override fun getSchedulesBySection(sectionId: UUID): List<TeacherScheduleResponse> =
        teacherScheduleMapper.toResponseList(
            teacherScheduleRepository.findBySectionId(sectionId)
        )

    override fun getSchedulesBySubject(subjectId: UUID): List<TeacherScheduleResponse> =
        teacherScheduleMapper.toResponseList(
            teacherScheduleRepository.findBySubjectId(subjectId)
        )

    override fun getSchedulesByTeacherAndSchoolYear(
        employeeId: UUID,
        schoolYear: String
    ): List<TeacherScheduleResponse> =
        teacherScheduleMapper.toResponseList(
            teacherScheduleRepository.findByEmployeeIdAndSchoolYear(employeeId, schoolYear)
        )

    override fun getSchedulesBySectionAndSchoolYear(
        sectionId: UUID,
        schoolYear: String
    ): List<TeacherScheduleResponse> =
        teacherScheduleMapper.toResponseList(
            teacherScheduleRepository.findBySectionIdAndSchoolYear(sectionId, schoolYear)
        )

    override fun updateSchedule(id: UUID, request: TeacherScheduleRequest): TeacherScheduleResponse {
        val existing = teacherScheduleRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Schedule not found with id: $id") }

        val section = sectionRepository.findById(request.sectionId)
            .orElseThrow { EntityNotFoundException("Section not found with id: ${request.sectionId}") }

        val subject = subjectRepository.findById(request.subjectId)
            .orElseThrow { EntityNotFoundException("Subject not found with id: ${request.subjectId}") }

        // Check conflicts for each day, excluding the current schedule's own slot
        request.days.forEach { day ->
            if (teacherScheduleRepository.hasConflictExcluding(
                    request.employeeId, day, request.timeStart, request.timeEnd, id
                )
            ) {
                throw IllegalArgumentException(
                    "Teacher has a conflicting schedule on ${day.name} at ${request.timeStart} - ${request.timeEnd}"
                )
            }

            if (teacherScheduleRepository.hasSectionConflictExcluding(
                    request.sectionId, day, request.timeStart, request.timeEnd, id
                )
            ) {
                throw IllegalArgumentException(
                    "Section has a conflicting schedule on ${day.name} at ${request.timeStart} - ${request.timeEnd}"
                )
            }
        }

        val updated = teacherScheduleRepository.save(
            TeacherSchedule(
                id = existing.id,
                employee = existing.employee,
                section = section,
                subject = subject,
                assignedBy = existing.assignedBy,
                dayOfWeek = request.days.first(),
                timeStart = request.timeStart,
                timeEnd = request.timeEnd,
                schoolYear = request.schoolYear,
                status = existing.status,
                createdAt = existing.createdAt
                // updatedAt is managed by @PreUpdate
            )
        )

        return teacherScheduleMapper.toResponse(updated)
    }

    override fun updateScheduleStatus(id: UUID, status: ScheduleStatus): TeacherScheduleResponse {
        val schedule = teacherScheduleRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Schedule not found with id: $id") }

        schedule.status = status
        // updatedAt is managed by @PreUpdate — no manual assignment needed

        return teacherScheduleMapper.toResponse(teacherScheduleRepository.save(schedule))
    }

    override fun deleteSchedule(id: UUID) {
        if (!teacherScheduleRepository.existsById(id)) {
            throw EntityNotFoundException("Schedule not found with id: $id")
        }
        teacherScheduleRepository.deleteById(id)
    }

    override fun getSchedulesByStudent(studentId: UUID): List<TeacherScheduleResponse> {
        val enrollment = studentEnrollmentRepository.findByStudentIdAndStatus (
            studentId, StudentStatus.ACTIVE
        ) ?: throw EntityNotFoundException("No active enrollment found for student: $studentId")

        return teacherScheduleMapper.toResponseList(
            teacherScheduleRepository.findBySectionId(enrollment.section.requireId())
        )
    }
}