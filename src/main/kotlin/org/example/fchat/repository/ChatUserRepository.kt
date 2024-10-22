package org.example.fchat.repository

import org.example.fchat.entity.Chat
import org.example.fchat.entity.ChatUser
import org.example.fchat.entity.User
import org.example.fchat.entity.enum.ChatType
import org.springframework.data.jpa.repository.JpaRepository

interface ChatUserRepository : JpaRepository<ChatUser, Long> {
    fun findAllByUser(username: User): List<ChatUser>
    fun findAllByChat(chat: Chat): List<ChatUser>
    fun findFirstByChatAndUser(chat: Chat, user: User): ChatUser?
    fun existsByUserInAndChatChatType(users: List<User>, chatType: ChatType): Boolean
    fun existsByChatAndUser(chat: Chat, user: User): Boolean
    fun deleteAllByChat(chat: Chat)
}