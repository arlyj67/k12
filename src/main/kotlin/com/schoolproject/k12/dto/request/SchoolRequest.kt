package com.schoolproject.k12.dto.request

data class SchoolRequest(
    val schoolName: String,
    val address: String,
    val contactNumber: String? = null,
    val email: String? = null,
    val principalName: String? = null,
    val isActive: Boolean = true
)