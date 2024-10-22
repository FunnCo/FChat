package org.example.fchat.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.example.fchat.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {

    @Operation(
        summary = "Регистрация нового пользователя",
        description = "Создание нового пользователя и получение JWT токена для аутентификации."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "JWT токен успешно создан."),
            ApiResponse(responseCode = "409", description = "Пользователь уже существует."),
            ApiResponse(responseCode = "400", description = "Некорректные данные запроса."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции.")
        ]
    )
    @PostMapping("/register")
    fun register(
        @Parameter(description = "Ник пользователя для регистрации", required = true)
        @RequestParam username: String,

        @Parameter(description = "Пароль для регистрации", required = true)
        @RequestParam password: String
    ): ResponseEntity<String> {
        val token = authService.register(username, password)
        return ResponseEntity.ok(token)
    }

    @Operation(
        summary = "Аутентификация пользователя",
        description = "Авторизация пользователя и получение JWT токена для доступа к защищенным ресурсам."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "JWT токен успешно создан."),
            ApiResponse(responseCode = "404", description = "Неверное имя пользователя или пароль."),
            ApiResponse(responseCode = "400", description = "Некорректные данные запроса."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции.")
        ]
    )
    @PostMapping("/login")
    fun login(
        @Parameter(description = "Имя пользователя для входа", required = true)
        @RequestParam username: String,

        @Parameter(description = "Пароль для входа", required = true)
        @RequestParam password: String
    ): ResponseEntity<String> {
        val token = authService.login(username, password)
        return ResponseEntity.ok(token)
    }
}