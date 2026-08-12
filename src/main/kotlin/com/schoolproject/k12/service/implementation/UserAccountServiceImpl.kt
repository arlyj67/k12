package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.entity.User
import com.schoolproject.k12.entity.AccountCredentials
import com.schoolproject.k12.model.Role
import com.schoolproject.k12.repository.SchoolRepository
import com.schoolproject.k12.repository.UserRepository
import com.schoolproject.k12.service.UserAccountService
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class UserAccountServiceImpl(
    private val userRepository: UserRepository,
    private val schoolRepository: SchoolRepository,
    private val passwordEncoder: PasswordEncoder
) : UserAccountService {

    override fun createStudentAccount(email: String, studentNumber: String, schoolId: UUID): AccountCredentials {
        val school = schoolRepository.findById(schoolId)
            .orElseThrow { NoSuchElementException("School not found") }

        val username = generateUsername("STU", studentNumber)
        val temporaryPassword = generateTemporaryPassword()

        val user = userRepository.save(
            User(
                school = school,
                username = username,
                email = email,
                passwordHash = passwordEncoder.encode(temporaryPassword)!!,
                role = Role.STUDENT,
                isActive = false
            )
        )

        return AccountCredentials(user = user, temporaryPassword = temporaryPassword)
    }

    override fun createEmployeeAccount(email: String?, employeeNumber: String, role: Role, schoolId: UUID): AccountCredentials {
        val school = schoolRepository.findById(schoolId)
            .orElseThrow { NoSuchElementException("School not found") }

        val username = generateUsername("EMP", employeeNumber)
        val temporaryPassword = generateTemporaryPassword()

        val user = userRepository.save(
            User(
                school = school,
                username = username,
                email = email,
                passwordHash = passwordEncoder.encode(temporaryPassword)!!,
                role = role,
                isActive = true
            )
        )

        return AccountCredentials(user = user, temporaryPassword = temporaryPassword)
    }

    private fun generateUsername(prefix: String, number: String): String = "$prefix-$number"

    private fun generateTemporaryPassword(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..10).map { chars.random() }.joinToString("")
    }
}