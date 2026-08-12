package com.schoolproject.k12.entity

data class AccountCredentials(
    val user: User,
    val temporaryPassword: String
)
