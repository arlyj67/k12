package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.request.EmployeeRequest
import com.schoolproject.k12.dto.response.EmployeeResponse
import com.schoolproject.k12.entity.Employee
import com.schoolproject.k12.entity.School
import com.schoolproject.k12.entity.User
import org.springframework.stereotype.Component

@Component
class EmployeeMapper {

    fun toEntity(dto: EmployeeRequest, user: User, school: School, employeeNumber: String): Employee {
        return Employee(
            user = user,
            school = school,
            employeeNumber = employeeNumber,
            firstName = dto.firstName,
            middleName = dto.middleName,
            lastName = dto.lastName,
            role = dto.role,
            contactNumber = dto.contactNumber,
            address = dto.address,
            email = dto.email,
            status = dto.status
        )
    }

    fun toResponse(employee: Employee): EmployeeResponse {
        // requireId() gives a clear error if called on an unpersisted entity,
        // replacing the silent NullPointerException from !!
        return EmployeeResponse(
            id = employee.id ?: error("Employee has not been persisted yet — id is null"),
            userId = employee.user.requireId(),
            schoolId = employee.school.requireId(),
            schoolName = employee.school.schoolName,
            employeeNumber = employee.employeeNumber,
            firstName = employee.firstName,
            middleName = employee.middleName,
            lastName = employee.lastName,
            fullName = employee.fullName,
            role = employee.role,
            contactNumber = employee.contactNumber,
            address = employee.address,
            email = employee.email,
            status = employee.status,
            createdAt = employee.createdAt,
            updatedAt = employee.updatedAt
        )
    }

    fun toResponseList(employees: List<Employee>): List<EmployeeResponse> =
        employees.map { toResponse(it) }
}