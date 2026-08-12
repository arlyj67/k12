package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.dto.request.StudentEnrollmentRequest
import com.schoolproject.k12.dto.response.StudentEnrollmentResponse
import com.schoolproject.k12.entity.StudentEnrollment
import com.schoolproject.k12.mapper.StudentEnrollmentMapper
import com.schoolproject.k12.model.StudentStatus
import com.schoolproject.k12.repository.SchoolRepository
import com.schoolproject.k12.repository.SectionRepository
import com.schoolproject.k12.repository.StudentEnrollmentRepository
import com.schoolproject.k12.repository.StudentRepository
import com.schoolproject.k12.service.StudentEnrollmentService
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class StudentEnrollmentServiceImpl(
    private val enrollmentRepository: StudentEnrollmentRepository,
    private val studentRepository: StudentRepository,
    private val sectionRepository: SectionRepository,
    private val schoolRepository: SchoolRepository,
    private val enrollmentMapper: StudentEnrollmentMapper
) : StudentEnrollmentService {

    override fun createEnrollment(request: StudentEnrollmentRequest): StudentEnrollmentResponse {
        val school = schoolRepository.findById(request.schoolId)
            .orElseThrow { EntityNotFoundException("School not found with id: ${request.schoolId}") }

        val student = studentRepository.findById(request.studentId)
            .orElseThrow { EntityNotFoundException("Student not found with id: ${request.studentId}") }

        val section = sectionRepository.findById(request.sectionId)
            .orElseThrow { EntityNotFoundException("Section not found with id: ${request.sectionId}") }

        val currentCount = enrollmentRepository.countBySectionIdAndStatus(
            request.sectionId, StudentStatus.ACTIVE
        )

        if (currentCount >= section.room.capacity) {
            throw IllegalArgumentException(
                "Section is already full. Room capacity is ${section.room.capacity}"
            )
        }

        // Check if student is already enrolled in the same school year
        if (enrollmentRepository.existsByStudentIdAndSchoolYear(request.studentId, request.schoolYear)) {
            throw IllegalArgumentException("Student is already enrolled for school year: ${request.schoolYear}")
        }

        // Save enrollment
        val enrollment = enrollmentRepository.save(
            enrollmentMapper.toEntity(request, student, section, school)
        )

        // Sync student status
        student.status = request.status
        studentRepository.save(student)

        return enrollmentMapper.toResponse(enrollment)
    }

    override fun updateEnrollmentStatus(id: UUID, status: StudentStatus): StudentEnrollmentResponse {
        val enrollment = enrollmentRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Enrollment not found with id: $id") }

        // Update enrollment status
        enrollment.status = status
        val updated = enrollmentRepository.save(enrollment)

        // Sync student status to match enrollment status
        val student = enrollment.student
        student.status = status
        studentRepository.save(student)

        return enrollmentMapper.toResponse(updated)
    }

    override fun transferStudent(enrollmentId: UUID, newSectionId: UUID): StudentEnrollmentResponse {
        val existing = enrollmentRepository.findById(enrollmentId)
            .orElseThrow { EntityNotFoundException("Enrollment not found with id: $enrollmentId") }

        val newSection = sectionRepository.findById(newSectionId)
            .orElseThrow { EntityNotFoundException("Section not found with id: $newSectionId") }

        // Mark old enrollment as transferred
        existing.status = StudentStatus.TRANSFERRED
        enrollmentRepository.save(existing)

        // Create new enrollment in the new section
        val newEnrollment = enrollmentRepository.save(
            StudentEnrollment(
                school = existing.school,
                student = existing.student,
                section = newSection,
                schoolYear = existing.schoolYear,
                enrollmentDate = existing.enrollmentDate,
                status = StudentStatus.ACTIVE
            )
        )

        // Student status stays ACTIVE during transfer
        val student = existing.student
        student.status = StudentStatus.ACTIVE
        studentRepository.save(student)

        return enrollmentMapper.toResponse(newEnrollment)
    }

    override fun getEnrollmentById(id: UUID): StudentEnrollmentResponse =
        enrollmentMapper.toResponse(
            enrollmentRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Enrollment not found with id: $id") }
        )

    override fun getEnrollmentsBySchool(schoolId: UUID): List<StudentEnrollmentResponse> =
        enrollmentMapper.toResponseList(
            enrollmentRepository.findBySchoolId(schoolId)
        )

    override fun getEnrollmentsByStudent(studentId: UUID): List<StudentEnrollmentResponse> =
        enrollmentMapper.toResponseList(
            enrollmentRepository.findByStudentId(studentId)
        )

    override fun getEnrollmentsBySection(sectionId: UUID): List<StudentEnrollmentResponse> =
        enrollmentMapper.toResponseList(
            enrollmentRepository.findBySectionId(sectionId)
        )

    override fun getEnrollmentsBySchoolYear(schoolId: UUID, schoolYear: String): List<StudentEnrollmentResponse> =
        enrollmentMapper.toResponseList(
            enrollmentRepository.findBySchoolIdAndSchoolYear(schoolId, schoolYear)
        )

    override fun getEnrollmentsByStatus(schoolId: UUID, status: StudentStatus): List<StudentEnrollmentResponse> =
        enrollmentMapper.toResponseList(
            enrollmentRepository.findBySchoolIdAndStatus(schoolId, status)
        )
}