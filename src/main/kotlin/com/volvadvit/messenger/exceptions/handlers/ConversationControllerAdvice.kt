package com.volvadvit.messenger.exceptions.handlers

import com.volvadvit.messenger.api.v1.models.ResponseMapper
import com.volvadvit.messenger.constants.ResponseConstants
import com.volvadvit.messenger.exceptions.InvalidConversationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.Timestamp
import java.time.Instant

@RestControllerAdvice
class ConversationControllerAdvice {

    @ExceptionHandler(InvalidConversationException::class)
    fun conversationInvalid(invalidConversationException: InvalidConversationException
    ) : ResponseEntity<ResponseMapper> {
        val res = ResponseMapper(
            HttpStatus.NOT_FOUND.value(),
            ResponseConstants.INVALID_CONVERSATION_ID.value,
            invalidConversationException.message,
            Timestamp.from(Instant.now()))
        return ResponseEntity.unprocessableEntity().body(res)
    }
}