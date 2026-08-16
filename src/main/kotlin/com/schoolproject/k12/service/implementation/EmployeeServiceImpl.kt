package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.dto.request.EmployeeRequest
import com.schoolproject.k12.dto.response.EmployeeResponse
import com.schoolproject.k12.entity.Employee
import com.schoolproject.k12.mapper.EmployeeMapper
import com.schoolproject.k12.model.EmployeeStatus
import com.schoolproject.k12.model.Role
import com.schoolproject.k12.repository.EmployeeRepository
import com.schoolproject.k12.repository.SchoolRepository
import com.schoolproject.k12.repository.UserRepository
import com.schoolproject.k12.service.EmployeeService
import com.schoolproject.k12.service.PdfGeneratorService
import com.schoolproject.k12.service.UserAccountService
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class EmployeeServiceImpl(
    private val employeeRepository: EmployeeRepository,
    private val schoolRepository: SchoolRepository,
    private val userRepository: UserRepository,
    private val employeeMapper: EmployeeMapper,
    private val userAccountService: UserAccountService,
    private val pdfGeneratorService: PdfGeneratorService,
    private val userNumberGenerator: UserNumberGenerator
) : EmployeeService {

    override fun createEmployee(dto: EmployeeRequest): EmployeeResponse {
        // Guard: only check email uniqueness when an email is actually provided
        dto.email?.let { email ->
            if (userRepository.existsByEmail(email)) {
                throw IllegalArgumentException("Email already exists: $email")
            }
        }

        val school = schoolRepository.findById(dto.schoolId)
            .orElseThrow { NoSuchElementException("School not found with id: ${dto.schoolId}") }

        val employeeNumber = userNumberGenerator.generate(dto.role)

        val credentials = userAccountService.createEmployeeAccount(
            email = dto.email,
            employeeNumber = employeeNumber,
            role = dto.role,
            schoolId = dto.schoolId
        )

        // Save employee once, then reuse the persisted entity — no second toEntity() call
        val employee = employeeRepository.save(
            employeeMapper.toEntity(dto, credentials.user, school, employeeNumber)
        )

        pdfGeneratorService.generateEmployeeAccountPDF(
            employee = employee,
            user = credentials.user,
            temporaryPassword = credentials.temporaryPassword
        )

        return employeeMapper.toResponse(employee)
    }

    override fun getAllEmployeesBySchool(schoolId: UUID): List<EmployeeResponse> =
        employeeMapper.toResponseList(employeeRepository.findBySchoolId(schoolId))

    override fun getEmployeeById(id: UUID): EmployeeResponse =
        employeeMapper.toResponse(
            employeeRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Employee not found with id: $id") }
        )

    override fun getEmployeeByNumber(employeeNumber: String): EmployeeResponse =
        employeeMapper.toResponse(
            employeeRepository.findByEmployeeNumber(employeeNumber)
                ?: throw EntityNotFoundException("Employee not found with employee number: $employeeNumber")
        )

    override fun getEmployeesByStatus(schoolId: UUID, status: EmployeeStatus): List<EmployeeResponse> =
        employeeMapper.toResponseList(employeeRepository.findBySchoolIdAndStatus(schoolId, status))

    override fun getEmployeesByRole(schoolId: UUID, role: Role): List<EmployeeResponse> =
        employeeMapper.toResponseList(employeeRepository.findBySchoolIdAndRole(schoolId, role))

    override fun updateEmployee(id: UUID, dto: EmployeeRequest): EmployeeResponse {
        val existing = employeeRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Employee not found with id: $id") }

        // Rebuild with all mutable fields from dto; preserve immutable identity fields from existing.
        // employeeNumber is never touched here — it's permanent once generated at creation.
        // School is intentionally preserved from existing — school transfers are a separate operation.
        // NOTE: email removed — Employee entity has no email field (it lives on User).
        // If you need to update the linked login email, that should go through UserAccountService
        // against `existing.user`, not through this entity rebuild.
        val updated = employeeRepository.save(
            Employee(
                id = existing.id,
                user = existing.user,
                school = existing.school,
                employeeNumber = existing.employeeNumber,
                firstName = dto.firstName,
                middleName = dto.middleName,
                lastName = dto.lastName,
                role = dto.role,
                contactNumber = dto.contactNumber,
                address = dto.address,
                status = dto.status,
                createdAt = existing.createdAt
                // updatedAt is managed by @PreUpdate — no need to set it manually
            )
        )

        return employeeMapper.toResponse(updated)
    }

    override fun deleteEmployee(id: UUID) {
        val employee = employeeRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Employee not found with id: $id") }

        // Deactivate the linked User account before removing the employee record.
        // This prevents the login from remaining active after the employee is deleted.
        val user = employee.user
        user.isActive = false
        userRepository.save(user)

        employeeRepository.delete(employee)
    }
}