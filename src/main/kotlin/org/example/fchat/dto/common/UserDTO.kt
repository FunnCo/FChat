package org.example.fchat.dto.common

data class UserDTO(
    val id: Long = -1,
    val username: String,
    val email: String? = null
)