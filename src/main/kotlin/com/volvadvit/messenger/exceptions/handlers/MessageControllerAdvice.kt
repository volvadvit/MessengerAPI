package com.volvadvit.messenger.exceptions.handlers

import com.volvadvit.messenger.api.v1.models.ResponseMapper
import com.volvadvit.messenger.constants.ResponseConstants
import com.volvadvit.messenger.exceptions.InvalidMessageException
import com.volvadvit.messenger.exceptions.MessageEmptyException
import com.volvadvit.messenger.exceptions.InvalidMessageRecipientException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant
import java.util.*

@ControllerAdvice
class MessageControllerAdvice {

    @ExceptionHandler(MessageEmptyException::class)
    fun messageEmpty(messageEmptyException: MessageEmptyException
    ) : ResponseEntity<ResponseMapper> {
        val res = ResponseMapper(
            HttpStatus.BAD_REQUEST.value(),
            ResponseConstants.MESSAGE_EMPTY.value,
            messageEmptyException.message,
            Date.from(Instant.now()))
        return ResponseEntity.unprocessableEntity().body(res)
    }

    @ExceptionHandler(InvalidMessageRecipientException::class)
    fun messageRecipientInvalid(invalidMessageRecipientException: InvalidMessageRecipientException
    ) : ResponseEntity<ResponseMapper> {
        val res = ResponseMapper(
            HttpStatus.BAD_REQUEST.value(),
            ResponseConstants.MESSAGE_RECIPIENT_INVALID.value,
            invalidMessageRecipientException.message,
            Date.from(Instant.now()))
        return ResponseEntity.unprocessableEntity().body(res)
    }

    @ExceptionHandler(InvalidMessageException::class)
    fun messageInvalid(invalidMessageException: InvalidMessageException
    ) : ResponseEntity<ResponseMapper> {
        val res = ResponseMapper(
            HttpStatus.BAD_REQUEST.value(),
            ResponseConstants.INVALID_MESSAGE_ID.value,
            invalidMessageException.message,
            Date.from(Instant.now()))
        return ResponseEntity.unprocessableEntity().body(res)
    }
}