package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.model.Role
import com.schoolproject.k12.repository.EmployeeRepository
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class UserNumberGenerator(
    private val employeeRepository: EmployeeRepository
) {

    companion object {
        private const val SUFFIX_LENGTH = 6
        private val PERIOD_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("MMyy")

        private val ALLOWED_CHARS: List<Char> =
            (('A'..'Z') + ('0'..'9')).filterNot { it in "OI01" }
    }

    fun generate(role: Role, maxAttempts: Int = 20): String {
        val prefix = rolePrefix(role)
        val period = LocalDate.now().format(PERIOD_FORMATTER)

        repeat(maxAttempts) {
            val candidate = "$prefix-$period-${randomSuffix()}"
            if (!employeeRepository.existsByEmployeeNumber(candidate)) {
                return candidate
            }
        }

        throw IllegalStateException(
            "Failed to generate a unique employee number after $maxAttempts attempts"
        )
    }

    private fun randomSuffix(): String =
        (1..SUFFIX_LENGTH)
            .map { ALLOWED_CHARS.random() }
            .joinToString("")

    private fun rolePrefix(role: Role): String = when (role) {
        Role.SUPER_ADMIN -> "SAD"
        Role.PRINCIPAL -> "PRN"
        Role.REGISTRAR -> "REG"
        Role.SECRETARY -> "SEC"
        Role.GUIDANCE_COUNSELOR -> "GDC"
        Role.LIBRARIAN -> "LIB"
        Role.NURSE -> "NUR"
        Role.SECURITY -> "SCG"
        Role.JANITOR -> "JAN"
        Role.TEACHER -> "TCH"
        Role.STUDENT -> throw IllegalArgumentException(
            "Students do not receive an employee number"
        )
    }
}