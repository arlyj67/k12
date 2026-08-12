package com.schoolproject.k12.service

import com.schoolproject.k12.dto.request.EmployeeRequest
import com.schoolproject.k12.dto.response.EmployeeResponse
import com.schoolproject.k12.model.EmployeeStatus
import com.schoolproject.k12.model.Role
import java.util.UUID

interface EmployeeService {
    fun getAllEmployeesBySchool(schoolId: UUID): List<EmployeeResponse>
    fun getEmployeeById(id: UUID): EmployeeResponse
    fun getEmployeeByNumber(employeeNumber: String): EmployeeResponse
    fun getEmployeesByStatus(schoolId: UUID, status: EmployeeStatus): List<EmployeeResponse>
    fun getEmployeesByRole(schoolId: UUID, role: Role): List<EmployeeResponse>
    fun createEmployee(dto: EmployeeRequest): EmployeeResponse
    fun updateEmployee(id: UUID, dto: EmployeeRequest): EmployeeResponse
    fun deleteEmployee(id: UUID)
}