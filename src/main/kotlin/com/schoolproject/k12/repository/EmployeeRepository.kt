package com.schoolproject.k12.repository

import com.schoolproject.k12.entity.Employee
import com.schoolproject.k12.model.EmployeeStatus
import com.schoolproject.k12.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface EmployeeRepository : JpaRepository<Employee, UUID> {

    fun findBySchoolId(schoolId: UUID): List<Employee>
    fun findBySchoolIdAndStatus(schoolId: UUID, status: EmployeeStatus): List<Employee>
    fun findBySchoolIdAndRole(schoolId: UUID, role: Role): List<Employee>
    fun existsByEmployeeNumber(employeeNumber: String): Boolean
    fun findByEmployeeNumber(employeeNumber: String): Employee?
    fun findByUserId(userId: UUID): Employee?
}