package com.schoolproject.k12.mapper

import com.schoolproject.k12.dto.request.UserRequest
import com.schoolproject.k12.dto.response.UserResponse
import com.schoolproject.k12.entity.School
import com.schoolproject.k12.entity.User
import org.springframework.stereotype.Component

@Component
class UserMapper {

    fun toEntity(dto: UserRequest, school: School, passwordHash: String): User {
        return User(
            school = school,
            username = dto.username,
            email = dto.email,
            passwordHash = passwordHash,
            role = dto.role,
            isActive = dto.isActive
        )
    }

    fun toResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            username = user.username,
            email = user.email,
            role = user.role,
            isActive = user.isActive,
            schoolName = user.school.schoolName,
            createdAt = user.createdAt
        )
    }
}