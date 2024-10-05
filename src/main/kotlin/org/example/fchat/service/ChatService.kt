package org.example.fchat.service

import org.example.fchat.model.Chat
import org.example.fchat.model.GroupChat
import org.example.fchat.model.User
import org.example.fchat.repository.ChatRepository
import org.example.fchat.repository.GroupChatRepository
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val groupChatRepository: GroupChatRepository
) {

    fun createPersonalChat(user1: User, user2: User): Chat {
        val chat = Chat(user1 = user1, user2 = user2)
        return chatRepository.save(chat)
    }

    fun createGroupChat(name: String, users: List<User>): GroupChat {
        val groupChat = GroupChat(name = name, users = users)
        return groupChatRepository.save(groupChat)
    }
}