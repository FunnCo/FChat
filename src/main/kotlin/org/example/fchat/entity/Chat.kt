package org.example.fchat.entity

import jakarta.persistence.*
import org.example.fchat.entity.enum.ChatType
import java.time.LocalDateTime

@Entity
@Table(name = "chats")
data class Chat(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    val chatType: ChatType,
    var name: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
