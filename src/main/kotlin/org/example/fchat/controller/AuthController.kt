package org.example.fchat.controller

import org.example.fchat.model.User
import org.example.fchat.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@RequestParam username: String, @RequestParam password: String): ResponseEntity<User> {
        val user = authService.register(username, password)
        return ResponseEntity.ok(user)
    }

    @PostMapping("/login")
    fun login(@RequestParam username: String, @RequestParam password: String): ResponseEntity<String> {
        // Логика авторизации с JWT
        return ResponseEntity.ok("Токен")
    }
}