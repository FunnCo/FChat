package org.example.fchat.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UserNotFoundException(message: String) : ResponseStatusException(HttpStatus.NOT_FOUND, message)
class UserAlreadyExistException(message: String) : ResponseStatusException(HttpStatus.CONFLICT, message)
class ChatNotFoundException(message: String) : ResponseStatusException(HttpStatus.NOT_FOUND, message)
class InvalidGroupChatException(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)
class InvalidMessageException(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)
class UserNotInChatException(message: String) :  ResponseStatusException(HttpStatus.BAD_REQUEST, message)
class UnableToChangeInfoException(message: String) : ResponseStatusException(HttpStatus.FORBIDDEN, message)
class ChatAlreadyExistException(message: String) : ResponseStatusException(HttpStatus.CONFLICT, message)