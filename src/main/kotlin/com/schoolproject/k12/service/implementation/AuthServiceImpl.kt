package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.config.JwtService
import com.schoolproject.k12.dto.request.AuthRequest
import com.schoolproject.k12.dto.request.RefreshRequest
import com.schoolproject.k12.dto.response.AuthResponse
import com.schoolproject.k12.entity.RefreshToken
import com.schoolproject.k12.entity.User
import com.schoolproject.k12.repository.RefreshTokenRepository
import com.schoolproject.k12.repository.UserRepository
import com.schoolproject.k12.service.AuthService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) : AuthService {

    override fun login(request: AuthRequest): AuthResponse {
        val user = userRepository.findByUsername(request.username)
            ?: throw NoSuchElementException("Invalid username or password")

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw IllegalArgumentException("Invalid username or password")
        }

        if (!user.isActive) {
            throw IllegalStateException("Account is not yet active. Please wait for registrar approval.")
        }

        return issueTokens(user)
    }

    @Transactional
    override fun refresh(request: RefreshRequest): AuthResponse {
        val tokenHash = jwtService.hashToken(request.refreshToken)
        val stored = refreshTokenRepository.findByTokenHash(tokenHash)
            ?: throw IllegalArgumentException("Invalid refresh token")

        if (!stored.isActive()) {
            throw IllegalStateException("Refresh token is expired or has been revoked")
        }

        // Rotation: the used token is burned the moment it's redeemed
        stored.revoked = true
        refreshTokenRepository.save(stored)

        val user = stored.user
        if (!user.isActive) {
            throw IllegalStateException("Account is not active")
        }

        return issueTokens(user)
    }

    @Transactional
    override fun logout(request: RefreshRequest) {
        val tokenHash = jwtService.hashToken(request.refreshToken)
        val stored = refreshTokenRepository.findByTokenHash(tokenHash) ?: return
        stored.revoked = true
        refreshTokenRepository.save(stored)
    }

    private fun issueTokens(user: User): AuthResponse {
        val accessToken = jwtService.generateAccessToken(user.requireId(), user.username, user.role.name, user.school.requireId())

        val rawRefreshToken = jwtService.generateRawRefreshToken()
        val refreshTokenEntity = RefreshToken(
            user = user,
            tokenHash = jwtService.hashToken(rawRefreshToken),
            expiresAt = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(jwtService.refreshExpiryMillis() / 1000)
        )
        refreshTokenRepository.save(refreshTokenEntity)

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = rawRefreshToken,
            expiresIn = jwtService.accessTokenExpirySeconds()
        )
    }
}