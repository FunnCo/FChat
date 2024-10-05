package org.example.fchat.repository

import org.example.fchat.model.Chat
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRepository : JpaRepository<Chat, Long>