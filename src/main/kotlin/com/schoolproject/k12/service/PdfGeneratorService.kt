package com.schoolproject.k12.service

import com.schoolproject.k12.entity.Employee
import com.schoolproject.k12.entity.Student
import com.schoolproject.k12.entity.User

interface PdfGeneratorService {

    fun generateStudentAccountPDF(student: Student, user: User, temporaryPassword: String)
    fun generateEmployeeAccountPDF(employee: Employee, user: User, temporaryPassword: String)

}