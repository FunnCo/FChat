package org.example.fchat.dto.common

import org.example.fchat.entity.Message
import java.time.LocalDateTime

data class MessageDTO(
    val id: Long?,
    val chatId: Long?,
    val sender: UserDTO,
    val content: String,
    val timestamp: LocalDateTime
) {
    companion object {
        fun fromMessage(message: Message): MessageDTO {
            return MessageDTO(
                id = message.id,
                chatId = message.chat.id,
                sender = message.sender.toDTO(),
                content = message.content,
                timestamp = message.timestamp
            )
        }
    }
}