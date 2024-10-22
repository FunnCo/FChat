package org.example.fchat.entity

import jakarta.persistence.*
import org.example.fchat.dto.common.UserDTO

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = -1,
    var username: String? = null,
    var password: String? = null,
    var email: String? = null
) {
    fun toDTO(): UserDTO {
        return UserDTO(
            id = this.id!!,
            username = this.username!!,
            email = this.email
        )
    }
}