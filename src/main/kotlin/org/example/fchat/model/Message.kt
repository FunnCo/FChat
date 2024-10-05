package org.example.fchat.model


import jakarta.persistence.*

@Entity
@Table(name = "messages")
class Message(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
    @ManyToOne val chat: Chat?,
    @ManyToOne val groupChat: GroupChat?,
    @ManyToOne val sender: User,
    val content: String
)