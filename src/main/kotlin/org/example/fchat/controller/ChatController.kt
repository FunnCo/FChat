package org.example.fchat.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.example.fchat.dto.common.ChatDTO
import org.example.fchat.dto.common.MessageDTO
import org.example.fchat.dto.request.CreateGroupChatRequest
import org.example.fchat.dto.request.InviteUsersRequest
import org.example.fchat.dto.request.SendMessageRequest
import org.example.fchat.service.ChatService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chats")
class ChatController(
    private val chatService: ChatService
) {

    @Operation(
        summary = "Создать персональный чат",
        description = "Создание персонального чата с другим пользователем и получение информации о чате."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Персональный чат успешно создан."),
            ApiResponse(responseCode = "404", description = "Пользователь не найден."),
            ApiResponse(responseCode = "409", description = "Персональный чат между указанными пользователями уже существует."),
            ApiResponse(responseCode = "400", description = "Некорректные данные запроса."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции.")
        ]
    )
    @PostMapping("/personal")
    fun createPersonalChat(
        @Parameter(description = "ID пользователя для создания чата", required = true)
        @RequestParam userId: Long,

        @Parameter(description = "JWT токен для аутентификации", required = true)
        @RequestHeader("Auth") token: String
    ): ResponseEntity<ChatDTO> {
        val chatDTO = chatService.createPersonalChat(userId, token)
        return ResponseEntity(chatDTO, HttpStatus.CREATED)
    }

    @Operation(
        summary = "Создать групповый чат",
        description = "Создание группового чата с указанными пользователями и заданным именем чата."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Групповой чат успешно создан."),
            ApiResponse(responseCode = "404", description = "Один или несколько пользователей не найдены."),
            ApiResponse(responseCode = "400", description = "Некорректные данные запроса или недостаточно участников."),
            ApiResponse(responseCode = "409", description = "Групповой чат с таким названием уже существует."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции.")
        ]
    )
    @PostMapping("/group")
    fun createGroupChat(
        @Parameter(description = "Запрос для создания группового чата", required = true)
        @RequestBody request: CreateGroupChatRequest,

        @Parameter(description = "JWT токен для аутентификации", required = true)
        @RequestHeader("Auth") token: String
    ): ResponseEntity<ChatDTO> {
        val chatDTO = chatService.createGroupChat(request.userIds, request.chatName, token)
        return ResponseEntity(chatDTO, HttpStatus.CREATED)
    }

    @Operation(
        summary = "Обновить название чата",
        description = "Изменение названия указанного чата."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Название чата успешно обновлено."),
            ApiResponse(responseCode = "404", description = "Чат не найден."),
            ApiResponse(responseCode = "400", description = "Некорректные данные запроса."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции.")
        ]
    )
    @PutMapping("/{chatId}/name")
    fun updateChatName(
        @Parameter(description = "ID чата для обновления названия", required = true)
        @PathVariable chatId: Long,

        @Parameter(description = "Новое название чата", required = true)
        @RequestParam newName: String,

        @Parameter(description = "JWT токен для аутентификации", required = true)
        @RequestHeader("Auth") token: String
    ): ResponseEntity<Unit> {
        chatService.updateChatName(chatId, newName, token)
        return ResponseEntity(HttpStatus.OK)
    }

    @Operation(
        summary = "Отправить сообщение в чат",
        description = "Отправка сообщения в указанный чат."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Сообщение успешно отправлено."),
            ApiResponse(responseCode = "404", description = "Чат или пользователь не найдены."),
            ApiResponse(responseCode = "400", description = "Некорректные данные сообщения."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции.")
        ]
    )
    @PostMapping("/{chatId}/messages")
    fun sendMessage(
        @Parameter(description = "ID чата для отправки сообщения", required = true)
        @PathVariable chatId: Long,

        @Parameter(description = "Запрос для отправки сообщения", required = true)
        @RequestBody request: SendMessageRequest,

        @Parameter(description = "JWT токен для аутентификации", required = true)
        @RequestHeader("Auth") token: String
    ): ResponseEntity<MessageDTO> {
        val messageDTO = chatService.sendMessage(chatId, request.content, token)
        return ResponseEntity(messageDTO, HttpStatus.CREATED)
    }

    @Operation(
        summary = "Удалить чат",
        description = "Удаление указанного чата."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Чат успешно удалён."),
            ApiResponse(responseCode = "404", description = "Чат не найден."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции.")
        ]
    )
    @DeleteMapping("/{chatId}")
    fun deleteChat(
        @Parameter(description = "ID чата для удаления", required = true)
        @PathVariable chatId: Long,

        @Parameter(description = "JWT токен для аутентификации", required = true)
        @RequestHeader("Auth") token: String
    ): ResponseEntity<Unit> {
        chatService.deleteChat(chatId, token)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Operation(
        summary = "Пригласить пользователей в групповой чат",
        description = "Приглашение указанных пользователей в существующий групповой чат."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Пользователи успешно приглашены."),
            ApiResponse(responseCode = "404", description = "Чат или один из пользователей не найдены."),
            ApiResponse(responseCode = "400", description = "Некорректные данные запроса."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции."),
            ApiResponse(responseCode = "409", description = "Пользователь уже состоит в чате.")
        ]
    )
    @PostMapping("/{chatId}/invite")
    fun inviteUsersToGroupChat(
        @Parameter(description = "ID группового чата для приглашения пользователей", required = true)
        @PathVariable chatId: Long,

        @Parameter(description = "Запрос для приглашения пользователей", required = true)
        @RequestBody request: InviteUsersRequest,

        @Parameter(description = "JWT токен для аутентификации", required = true)
        @RequestHeader("Auth") token: String
    ): ResponseEntity<Unit> {
        chatService.inviteUsersToGroupChat(chatId, request.userIds, token)
        return ResponseEntity(HttpStatus.OK)
    }

    @Operation(
        summary = "Получить чаты пользователя",
        description = "Получение списка всех чатов, в которых состоит текущий пользователь."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Список чатов успешно получен."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции.")
        ]
    )
    @GetMapping("/my")
    fun getUserChats(
        @Parameter(description = "JWT токен для аутентификации", required = true)
        @RequestHeader("Auth") token: String
    ): ResponseEntity<List<ChatDTO>> {
        val chats = chatService.getUserChats(token)
        return ResponseEntity(chats, HttpStatus.OK)
    }

    @Operation(
        summary = "Получить сообщения из чата",
        description = "Получение всех сообщений из указанного чата."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Сообщения успешно получены."),
            ApiResponse(responseCode = "404", description = "Чат не найден."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции.")
        ]
    )
    @GetMapping("/{chatId}/messages")
    fun getMessagesFromChat(
        @Parameter(description = "JWT токен для аутентификации", required = true)
        @RequestHeader("Auth") token: String,

        @Parameter(description = "ID чата для получения сообщений", required = true)
        @PathVariable chatId: Long
    ): ResponseEntity<List<MessageDTO>> {
        val messages = chatService.getMessagesFromChat(chatId, token)
        return ResponseEntity(messages, HttpStatus.OK)
    }

    @Operation(
        summary = "Покинуть чат",
        description = "Выход из указанного чата."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Пользователь успешно покинул чат."),
            ApiResponse(responseCode = "404", description = "Чат не найден."),
            ApiResponse(responseCode = "400", description = "Пользователь не является участником чата."),
            ApiResponse(responseCode = "403", description = "Нет доступа к выполнению операции.")
        ]
    )
    @PostMapping("/{chatId}/leave")
    fun leaveChat(
        @Parameter(description = "ID чата для выхода", required = true)
        @PathVariable chatId: Long,

        @Parameter(description = "JWT токен для аутентификации", required = true)
        @RequestHeader("Auth") token: String
    ): ResponseEntity<Unit> {
        chatService.leaveChat(chatId, token)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}