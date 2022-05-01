package com.volvadvit.messenger.exceptions.handlers

import com.volvadvit.messenger.constants.ErrorResponse
import com.volvadvit.messenger.exceptions.ConversationInvalidException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ConversationControllerAdvice {

    @ExceptionHandler(ConversationInvalidException::class)
    fun usernameUnavailable(conversationInvalidException: ConversationInvalidException
    ) : ResponseEntity<ErrorResponse> {
        val res = ErrorResponse("", conversationInvalidException.message)
        return ResponseEntity.unprocessableEntity().body(res)
    }
}