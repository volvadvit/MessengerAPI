package com.volvadvit.messenger.api.components

import com.volvadvit.messenger.api.constants.ErrorResponse
import com.volvadvit.messenger.api.constants.ResponseConstants
import com.volvadvit.messenger.api.exceptions.ConversationInvalidException
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