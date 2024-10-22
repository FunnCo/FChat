package org.example.fchat.repository

import org.example.fchat.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun findAllByEmailContainingIgnoreCase(emailPart: String): List<User>
}