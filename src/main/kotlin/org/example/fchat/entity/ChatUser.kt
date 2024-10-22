package org.example.fchat.entity

import jakarta.persistence.*

@Entity
@Table(name = "chat_users")
data class ChatUser(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "chat_id")
    val chat: Chat,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User
)