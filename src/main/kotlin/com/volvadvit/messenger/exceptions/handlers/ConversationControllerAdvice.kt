package com.volvadvit.messenger.exceptions.handlers

import com.volvadvit.messenger.api.v1.models.ResponseMapper
import com.volvadvit.messenger.exceptions.ConversationInvalidException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ConversationControllerAdvice {

    @ExceptionHandler(ConversationInvalidException::class)
    fun usernameUnavailable(conversationInvalidException: ConversationInvalidException
    ) : ResponseEntity<ResponseMapper> {
        val res = ResponseMapper(HttpStatus.NOT_FOUND.value(),
            "User with provided username doesn't exist",
            conversationInvalidException.message)
        return ResponseEntity.unprocessableEntity().body(res)
    }
}