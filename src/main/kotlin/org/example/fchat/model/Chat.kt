package org.example.fchat.model

import jakarta.persistence.*

@Entity
@Table(name = "chats")
class Chat(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
    @ManyToOne val user1: User,
    @ManyToOne val user2: User
)