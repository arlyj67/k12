package com.schoolproject.k12.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import java.util.Date
import java.util.UUID

@Service
class JwtService(
    private val jwtProperties: JwtProperties
) {

    private val signingKey by lazy {
        Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())
    }

    private val secureRandom = SecureRandom()

    fun generateAccessToken(userId: UUID, username: String, role: String, schoolId: UUID): String {
        return Jwts.builder()
            .subject(username)
            .claim("userId", userId.toString())
            .claim("role", role)
            .claim("schoolId", schoolId.toString())
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + jwtProperties.expiration))
            .signWith(signingKey)
            .compact()
    }

    fun extractUsername(token: String): String =
        getClaims(token).subject

    fun extractSchoolId(token: String): UUID =
        UUID.fromString(getClaims(token)["schoolId"] as String)

    fun extractRole(token: String): String =
        getClaims(token)["role"] as String

    fun isTokenValid(token: String): Boolean {
        return try {
            getClaims(token).expiration.after(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun accessTokenExpirySeconds(): Long = jwtProperties.expiration / 1000

    fun generateRawRefreshToken(): String {
        val bytes = ByteArray(64)
        secureRandom.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    fun hashToken(rawToken: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(rawToken.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun refreshExpiryMillis(): Long = jwtProperties.refreshExpiration

    private fun getClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
}