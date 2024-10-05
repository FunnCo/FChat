package org.example.fchat.service

import org.example.fchat.model.User
import org.example.fchat.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): User {
        return userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found")
    }

    fun register(username: String, password: String): User {
        val user = User(username = username, password = password)
        return userRepository.save(user)
    }
}