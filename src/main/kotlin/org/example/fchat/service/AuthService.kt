package org.example.fchat.service


import org.springframework.stereotype.Service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.example.fchat.entity.User
import org.example.fchat.exceptions.UserAlreadyExistException
import org.example.fchat.exceptions.UserNotFoundException
import org.example.fchat.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import java.util.concurrent.TimeUnit


@Service
class AuthService(val userRepository: UserRepository, val passwordEncoder: PasswordEncoder) {

    @Value("\${secret}")
    private lateinit var secretKey: String

    fun register(username: String, password: String): String {
        if (userRepository.existsByUsername(username)) {
            throw UserAlreadyExistException("User with such username already exists")
        }
        val encodedPassword = passwordEncoder.encode(password)
        var user = User(username = username, password = encodedPassword)
        user = userRepository.save(user)
        return generateToken(user.id.toString())
    }

    fun login(username: String, password: String): String {
        val user = userRepository.findByUsername(username)
        if (user != null && passwordEncoder.matches(password, user.password)) {
            return generateToken(user.id.toString())
        } else {
            throw UserNotFoundException("Invalid username or password")
        }
    }

    fun generateToken(userId: String): String {
        val claims: MutableMap<String, Any> = HashMap()
        return createToken(claims, userId)
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        val now = Date(System.currentTimeMillis())
        val expiryDate = Date(now.time + TimeUnit.DAYS.toMillis(1))
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(Keys.hmacShaKeyFor(secretKey.encodeToByteArray()))
            .compact()
    }

    fun extractUserId(token: String): Long {
        return extractAllClaims(token.substringAfter("Bearer ")).subject.toLong()
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.encodeToByteArray())).build().parseClaimsJws(token).body
    }
}