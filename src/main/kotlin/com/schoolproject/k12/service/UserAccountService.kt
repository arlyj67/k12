package com.schoolproject.k12.service

import com.schoolproject.k12.entity.AccountCredentials
import com.schoolproject.k12.entity.User
import com.schoolproject.k12.model.Role
import java.util.UUID

interface UserAccountService {
    fun createStudentAccount(email: String, studentNumber: String, schoolId: UUID): AccountCredentials
    fun createEmployeeAccount(email: String?, employeeNumber: String, role: Role, schoolId: UUID): AccountCredentials
}