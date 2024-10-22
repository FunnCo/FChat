package org.example.fchat.service

import org.example.fchat.dto.common.UserDTO
import org.example.fchat.entity.User
import org.example.fchat.exceptions.UnableToChangeInfoException
import org.example.fchat.exceptions.UserAlreadyExistException
import org.example.fchat.exceptions.UserNotFoundException
import org.example.fchat.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val authService: AuthService) {

    fun findByEmail(emailPart: String): List<UserDTO> {
        require(emailPart.length >= 3) { "Email part must contain at least 3 characters." }
        return userRepository.findAllByEmailContainingIgnoreCase(emailPart).stream().map { it.toDTO() }.toList()
    }

    fun findByNickname(nickname: String): UserDTO? {
        return userRepository.findByUsername(nickname)?.toDTO() ?: throw IllegalArgumentException("User not found")
    }

    fun getCurrentUser(authorizationToken: String): User {
        try{
            return userRepository.getReferenceById(authService.extractUserId(authorizationToken))
        } catch (any: Exception){
            throw UserNotFoundException("User not found")
        }
    }

    fun getCurrentUserDTO(authorizationToken: String): UserDTO? {
        return getCurrentUser(authorizationToken).toDTO()
    }

    fun updateUser(authorizationToken: String, newData: User): UserDTO {
        val userToUpdate = userRepository.getReferenceById(authService.extractUserId(authorizationToken))
            ?: throw UserNotFoundException("User not found")

        if(newData.id != -1L && userToUpdate.id != newData.id){
            throw UnableToChangeInfoException("You can change only your user info, and cannot change your userId")
        }

        if(newData.email != null && userRepository.existsByEmail(newData.email!!)){
            throw UserAlreadyExistException("This email is taken by someone else")
        }

        if(newData.username != null && userRepository.existsByUsername(newData.username!!)){
            throw UserAlreadyExistException("This username is taken by someone else")
        }

        userToUpdate.username = newData.username ?: userToUpdate.username
        userToUpdate.password = newData.password ?: userToUpdate.password
        userToUpdate.email = newData.email ?: userToUpdate.username

        return userRepository.save(userToUpdate).toDTO()
    }
}