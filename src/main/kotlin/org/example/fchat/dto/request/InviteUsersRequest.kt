package org.example.fchat.dto.request

data class InviteUsersRequest(
    var userIds: List<Long> = emptyList()
)