package org.example.fchat.model

import jakarta.persistence.*

@Entity
@Table(name = "group_chats")
class GroupChat(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
    val name: String,
    @ManyToMany val users: List<User>
)