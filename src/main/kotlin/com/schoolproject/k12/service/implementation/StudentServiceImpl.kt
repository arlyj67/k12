package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.dto.request.StudentRequest
import com.schoolproject.k12.dto.response.StudentResponse
import com.schoolproject.k12.dto.response.StudentListResponse
import com.schoolproject.k12.dto.response.StudentPendingResponse
import com.schoolproject.k12.entity.Student
import com.schoolproject.k12.entity.User
import com.schoolproject.k12.mapper.StudentMapper
import com.schoolproject.k12.model.StudentStatus
import com.schoolproject.k12.repository.SchoolRepository
import com.schoolproject.k12.repository.StudentRepository
import com.schoolproject.k12.repository.UserRepository
import com.schoolproject.k12.service.PdfGeneratorService
import com.schoolproject.k12.service.StudentService
import com.schoolproject.k12.service.UserAccountService
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
@Transactional
class StudentServiceImpl(
    private val studentRepository: StudentRepository,
    private val userRepository: UserRepository,
    private val schoolRepository: SchoolRepository,
    private val studentMapper: StudentMapper,
    private val userAccountService: UserAccountService,
    private val pdfGeneratorService: PdfGeneratorService
) : StudentService {

    override fun generateStudentNumber(schoolId: UUID, gradeLevel: Int): String {
        val year = LocalDate.now().year
        val prefix = "$year-G$gradeLevel-"

        val latest = studentRepository.findLatestStudentNumber(schoolId, prefix)

        val nextSequence = if (latest != null) {
            // Extract the sequence part and increment
            val lastSequence = latest.removePrefix(prefix).toIntOrNull() ?: 0
            lastSequence + 1
        } else {
            // First student for this school + year + grade level
            1
        }

        // Format: 2024-G7-00001
        return "$prefix${nextSequence.toString().padStart(5, '0')}"
    }


    override fun registerStudent(dto: StudentRequest): StudentResponse {
        if (userRepository.existsByEmail(dto.email)) {
            throw IllegalArgumentException("Email already exists: ${dto.email}")
        }

        val studentNumber = generateStudentNumber(dto.schoolId, dto.studentLevel)

        val school = schoolRepository.findById(dto.schoolId)
            .orElseThrow { NoSuchElementException("School not found") }

        // Create account — returns User + temporaryPassword
        val credentials = userAccountService.createStudentAccount(
            email = dto.email,
            studentNumber = studentNumber,
            schoolId = dto.schoolId
        )

        // Save student
        val student = studentRepository.save(
            studentMapper.toEntity(dto, credentials.user, school, studentNumber)
        )

        // Generate PDF with credentials
        pdfGeneratorService.generateStudentAccountPDF(
            student = student,
            user = credentials.user,
            temporaryPassword = credentials.temporaryPassword
        )

        return studentMapper.toResponse(student)

    }

    override fun activateStudent(studentId: UUID): StudentResponse {
        val student = studentRepository.findById(studentId)
            .orElseThrow { NoSuchElementException("Student not found") }

        if (student.status != StudentStatus.PENDING) {
            throw IllegalStateException("Student is not in PENDING status")
        }

        student.user.isActive = true
        userRepository.save<User>(student.user)

        student.status = StudentStatus.ACTIVE
        studentRepository.save<Student>(student)

        return studentMapper.toResponse(student)
    }

    override fun getPendingStudents(schoolId: UUID): List<StudentPendingResponse> =
        studentMapper.toPendingResponseList(
            studentRepository.findBySchoolIdAndStatus(schoolId, StudentStatus.PENDING)
        )

    override fun getStudentsListBySchool(schoolId: UUID): List<StudentListResponse> =
        studentMapper.toListResponseList(studentRepository.findBySchoolId(schoolId))

    override fun getStudentsBySchool(schoolId: UUID): List<StudentResponse> =
        studentRepository.findBySchoolId(schoolId)
            .map { studentMapper.toResponse(it) }

    override fun getStudentById(id: UUID): StudentResponse =
        studentRepository.findById(id)
            .map { studentMapper.toResponse(it) }
            .orElseThrow { NoSuchElementException("Student not found") }

    override fun getStudentByNumber(studentNumber: String): StudentResponse =
        studentMapper.toResponse(
            studentRepository.findByStudentNumber(studentNumber)
                ?: throw NoSuchElementException("Student not found: $studentNumber")
        )

    override fun getStudentByUserId(userId: UUID): StudentResponse {
        val student = studentRepository.findByUserId(userId)
            ?: throw EntityNotFoundException("Student not found with user id: $userId")
        return studentMapper.toResponse(student)
    }

}