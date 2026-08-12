package com.schoolproject.k12.model

object ErrorCode {

    // Student
    const val STUDENT_NOT_FOUND = "STUDENT_NOT_FOUND"
    const val STUDENT_NUMBER_ALREADY_EXISTS = "STUDENT_NUMBER_ALREADY_EXISTS"
    const val STUDENT_NOT_PENDING = "STUDENT_NOT_PENDING"

    // Employee
    const val EMPLOYEE_NOT_FOUND = "EMPLOYEE_NOT_FOUND"
    const val EMPLOYEE_NUMBER_ALREADY_EXISTS = "EMPLOYEE_NUMBER_ALREADY_EXISTS"

    // School
    const val SCHOOL_NOT_FOUND = "SCHOOL_NOT_FOUND"
    const val SCHOOL_ALREADY_EXISTS = "SCHOOL_ALREADY_EXISTS"

    // User
    const val EMAIL_ALREADY_EXISTS = "EMAIL_ALREADY_EXISTS"
    const val USER_NOT_FOUND = "USER_NOT_FOUND"

    // Validation
    const val VALIDATION_FAILED = "VALIDATION_FAILED"

    // General
    const val INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR"
}