package org.example.fchat.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "messages")
data class Message(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "chat_id")
    val chat: Chat,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val sender: User,
    val content: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)