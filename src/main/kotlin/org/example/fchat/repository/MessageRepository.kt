package org.example.fchat.repository

import org.example.fchat.entity.Chat
import org.example.fchat.entity.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, Long>{
    fun findByChat(chat : Chat) : List<Message>
}