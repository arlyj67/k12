package com.schoolproject.k12.service

import com.schoolproject.k12.dto.request.AuthRequest
import com.schoolproject.k12.dto.request.RefreshRequest
import com.schoolproject.k12.dto.response.AuthResponse

interface AuthService {
    fun login(request: AuthRequest): AuthResponse
    fun refresh(request: RefreshRequest): AuthResponse
    fun logout(request: RefreshRequest)
}