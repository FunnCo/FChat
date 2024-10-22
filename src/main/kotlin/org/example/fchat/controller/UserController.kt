package org.example.fchat.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.example.fchat.dto.common.UserDTO
import org.example.fchat.entity.User
import org.example.fchat.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @Operation(
        summary = "Найти пользователя по части email",
        description = "Поиск пользователей, чьи email содержат указанную часть почты."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Пользователи успешно найдены."),
            ApiResponse(responseCode = "400", description = "Некорректные данные запроса."),
            ApiResponse(responseCode = "404", description = "Пользователи не найдены."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции.")
        ]
    )
    @GetMapping("/find/email")
    fun findUserByEmail(
        @Parameter(description = "JWT токен для аутентификации", required = true)
        @RequestHeader("Auth") token: String,

        @Parameter(description = "Часть email для поиска", required = true)
        @RequestParam email: String?
    ): ResponseEntity<List<UserDTO>> {
        return email?.let {
            ResponseEntity(userService.findByEmail(it), HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @Operation(
        summary = "Найти пользователя по никнейму",
        description = "Поиск пользователя по полному никнейму."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Пользователь успешно найден."),
            ApiResponse(responseCode = "400", description = "Некорректные данные запроса."),
            ApiResponse(responseCode = "404", description = "Пользователь не найден."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции.")
        ]
    )
    @GetMapping("/find/nickname")
    fun findUserByNickname(
        @Parameter(description = "JWT токен для аутентификации", required = true)
        @RequestHeader("Auth") token: String,

        @Parameter(description = "Никнейм пользователя для поиска", required = true)
        @RequestParam nickname: String?
    ): ResponseEntity<UserDTO?> {
        return nickname?.let {
            ResponseEntity(userService.findByNickname(it), HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @Operation(
        summary = "Получить информацию о текущем пользователе",
        description = "Получение детальной информации о пользователе, связанного с JWT токеном."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Информация о пользователе успешно получена."),
            ApiResponse(responseCode = "404", description = "Пользователь не найден."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции.")
        ]
    )
    @GetMapping("/myinfo")
    fun getMyInfo(
        @Parameter(description = "JWT токен для аутентификации", required = true)
        @RequestHeader("Auth") token: String
    ): ResponseEntity<UserDTO?> {
        val user = userService.getCurrentUserDTO(token)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @Operation(
        summary = "Обновить информацию о текущем пользователе",
        description = "Обновление информации о пользователе, связанного с JWT токеном."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Информация о пользователе успешно обновлена."),
            ApiResponse(responseCode = "400", description = "Некорректные данные запроса."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции."),
            ApiResponse(responseCode = "404", description = "Пользователь не найден."),
            ApiResponse(responseCode = "409", description = "Конфликт данных при обновлении.")
        ]
    )
    @PutMapping("/myinfo")
    fun updateMyInfo(
        @Parameter(description = "JWT токен для аутентификации", required = true)
        @RequestHeader("Auth") token: String,

        @Parameter(description = "Новые данные пользователя для обновления", required = true)
        @RequestBody user: User
    ): ResponseEntity<UserDTO> {
        val updatedUser = userService.updateUser(token, user)
        return ResponseEntity(updatedUser, HttpStatus.OK)
    }
}