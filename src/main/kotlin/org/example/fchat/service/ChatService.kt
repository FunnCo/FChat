package org.example.fchat.service

import org.example.fchat.dto.common.ChatDTO
import org.example.fchat.dto.common.MessageDTO
import org.example.fchat.entity.Chat
import org.example.fchat.entity.ChatUser
import org.example.fchat.entity.Message
import org.example.fchat.entity.User
import org.example.fchat.entity.enum.ChatType
import org.example.fchat.exceptions.*
import org.example.fchat.repository.ChatRepository
import org.example.fchat.repository.ChatUserRepository
import org.example.fchat.repository.MessageRepository
import org.example.fchat.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val chatUserRepository: ChatUserRepository,
    private val messageRepository: MessageRepository,
    private val userService: UserService
) {

    @Transactional
    fun createPersonalChat(userId2: Long, token: String): ChatDTO {
        val currentUser = userService.getCurrentUser(token)
        val user2 = userRepository.findById(userId2).orElseThrow {
            throw UserNotFoundException("User with ID $userId2 not found")
        }

        if (chatUserRepository.existsByUserInAndChatChatType(listOf(currentUser, user2), ChatType.PERSONAL)){
            throw ChatAlreadyExistException("Personal chat between these two users already exists")
        }

        val chat = Chat(chatType = ChatType.PERSONAL, name = "${currentUser.username} - ${user2.username}")
        val savedChat = chatRepository.save(chat)

        chatUserRepository.save(ChatUser(chat = savedChat, user = currentUser))
        chatUserRepository.save(ChatUser(chat = savedChat, user = user2))

        return ChatDTO.fromChat(savedChat, listOf(currentUser, user2))
    }

    @Transactional
    fun createGroupChat(userIds: List<Long>, chatName: String, token: String): ChatDTO {
        if (userIds.isEmpty()) {
            throw InvalidGroupChatException("At least one user is required to create a group chat")
        }

        val currentUser = userService.getCurrentUser(token)
        val chat = Chat(chatType = ChatType.GROUP, name = chatName)
        val savedChat = chatRepository.save(chat)

        chatUserRepository.save(ChatUser(chat = savedChat, user = currentUser))

        val users = mutableListOf(currentUser)
        userIds.forEach { userId ->
            val user = userRepository.findById(userId).orElseThrow {
                throw UserNotFoundException("User with ID $userId not found")
            }
            if(!chatUserRepository.existsByChatAndUser(chat, user)){
                chatUserRepository.save(ChatUser(chat = savedChat, user = user))
                users.add(user)
            }
        }

        return ChatDTO.fromChat(savedChat, users)
    }

    @Transactional
    fun updateChatName(chatId: Long, newName: String, token: String) {
        val currentUser = userService.getCurrentUser(token)
        ensureUserInChat(chatId, currentUser)

        val chat = chatRepository.findById(chatId).orElseThrow {
            throw ChatNotFoundException("Chat with ID $chatId not found")
        }

        chat.name = newName
        chatRepository.save(chat)
    }

    @Transactional
    fun sendMessage(chatId: Long, content: String, token: String): MessageDTO {
        if (content.isBlank()) {
            throw InvalidMessageException("Message content cannot be empty")
        }

        val currentUser = userService.getCurrentUser(token)
        ensureUserInChat(chatId, currentUser)

        val chat = chatRepository.findById(chatId).orElseThrow {
            throw ChatNotFoundException("Chat with ID $chatId not found")
        }

        val message = Message(chat = chat, sender = currentUser, content = content)
        val savedMessage = messageRepository.save(message)
        return MessageDTO.fromMessage(savedMessage)
    }

    @Transactional
    fun deleteChat(chatId: Long, token: String) {
        val currentUser = userService.getCurrentUser(token)
        ensureUserInChat(chatId, currentUser)

        if (!chatRepository.existsById(chatId)) {
            throw ChatNotFoundException("Chat with ID $chatId not found")
        }

        val chatToDelete = chatRepository.getReferenceById(chatId)
        chatUserRepository.deleteAllByChat(chatToDelete)

        chatRepository.deleteById(chatId)
    }

    @Transactional
    fun inviteUsersToGroupChat(chatId: Long, userIds: List<Long>, token: String) {
        val chat = chatRepository.findById(chatId).orElseThrow {
            throw ChatNotFoundException("Chat with ID $chatId not found")
        }

        if (chat.chatType != ChatType.GROUP) {
            throw InvalidGroupChatException("Chat with ID $chatId is not a group chat")
        }

        val currentUser = userService.getCurrentUser(token)
        ensureUserInChat(chatId, currentUser)

        userIds.forEach { userId ->
            val user = userRepository.findById(userId).orElseThrow {
                throw UserNotFoundException("User with ID $userId not found")
            }
            if(!chatUserRepository.existsByChatAndUser(chat, user)){
                chatUserRepository.save(ChatUser(chat = chat, user = user))
            }
        }
    }

    fun getUserChats(token: String): List<ChatDTO> {
        val currentUser = userService.getCurrentUser(token)
        return chatUserRepository
            .findAllByUser(currentUser)
            .map { ChatDTO.fromChat(it.chat, chatUserRepository.findAllByChat(it.chat).map(ChatUser::user)) }
    }

    fun getMessagesFromChat(chatId: Long, token: String): List<MessageDTO> {
        val currentUser = userService.getCurrentUser(token)
        ensureUserInChat(chatId, currentUser)
        val chat = chatRepository.findById(chatId).orElseThrow {
            throw ChatNotFoundException("Chat with ID $chatId not found")
        }

        return messageRepository.findByChat(chat).map { MessageDTO.fromMessage(it) }
    }

    @Transactional
    fun leaveChat(chatId: Long, token: String) {
        val currentUser = userService.getCurrentUser(token)
        ensureUserInChat(chatId, currentUser)

        val chat = chatRepository.findById(chatId).orElseThrow {
            throw ChatNotFoundException("Chat with ID $chatId not found")
        }

        val chatUser = chatUserRepository.findFirstByChatAndUser(chat, currentUser)
            ?: throw UserNotInChatException("User is not a member of this chat")

        chatUserRepository.delete(chatUser)
    }

    private fun ensureUserInChat(chatId: Long, user: User) {
        val chat = chatRepository.findById(chatId).orElseThrow {
            throw ChatNotFoundException("Chat with ID $chatId not found")
        }
        val chatUser = chatUserRepository.findFirstByChatAndUser(chat, user)
            ?: throw UserNotInChatException("User is not a member of this chat")
    }
}