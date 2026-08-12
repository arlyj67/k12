package com.schoolproject.k12.controller

import com.schoolproject.k12.dto.request.EmployeeRequest
import com.schoolproject.k12.dto.response.EmployeeResponse
import com.schoolproject.k12.model.EmployeeStatus
import com.schoolproject.k12.model.Role
import com.schoolproject.k12.service.EmployeeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/employees")
class EmployeeController(
    private val employeeService: EmployeeService
) {

    @PostMapping
    fun create(@RequestBody dto: EmployeeRequest): ResponseEntity<EmployeeResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(dto))

    @GetMapping("/school/{schoolId}")
    fun getAllBySchool(@PathVariable schoolId: UUID): ResponseEntity<List<EmployeeResponse>> =
        ResponseEntity.ok(employeeService.getAllEmployeesBySchool(schoolId))

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<EmployeeResponse> =
        ResponseEntity.ok(employeeService.getEmployeeById(id))

    @GetMapping("/number/{employeeNumber}")
    fun getByEmployeeNumber(@PathVariable employeeNumber: String): ResponseEntity<EmployeeResponse> =
        ResponseEntity.ok(employeeService.getEmployeeByNumber(employeeNumber))

    @GetMapping("/school/{schoolId}/status")
    fun getByStatus(
        @PathVariable schoolId: UUID,
        @RequestParam status: EmployeeStatus
    ): ResponseEntity<List<EmployeeResponse>> =
        ResponseEntity.ok(employeeService.getEmployeesByStatus(schoolId, status))

    @GetMapping("/school/{schoolId}/role")
    fun getByRole(
        @PathVariable schoolId: UUID,
        @RequestParam role: Role
    ): ResponseEntity<List<EmployeeResponse>> =
        ResponseEntity.ok(employeeService.getEmployeesByRole(schoolId, role))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody dto: EmployeeRequest
    ): ResponseEntity<EmployeeResponse> =
        ResponseEntity.ok(employeeService.updateEmployee(id, dto))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        employeeService.deleteEmployee(id)
        return ResponseEntity.noContent().build()
    }
}