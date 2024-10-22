package org.example.fchat.dto.request

data class CreateGroupChatRequest(
    val userIds: List<Long>,
    val chatName: String
)