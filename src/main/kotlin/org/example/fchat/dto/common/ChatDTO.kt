package org.example.fchat.dto.common

import org.example.fchat.entity.Chat
import org.example.fchat.entity.User
import org.example.fchat.entity.enum.ChatType

data class ChatDTO(
    val id: Long?,
    val chatType: ChatType,
    val name: String?,
    val users: List<UserDTO>
) {
    companion object {
        fun fromChat(chat: Chat, users: List<User>): ChatDTO {
            return ChatDTO(
                id = chat.id,
                chatType = chat.chatType,
                name = chat.name,
                users = users.map(User::toDTO)
            )
        }
    }
}