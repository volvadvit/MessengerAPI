package com.volvadvit.messenger.exceptions.handlers

import com.volvadvit.messenger.api.v1.models.ResponseMapper
import com.volvadvit.messenger.constants.ResponseConstants
import com.volvadvit.messenger.exceptions.MessageEmptyException
import com.volvadvit.messenger.exceptions.MessageRecipientInvalidException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class MessageControllerAdvice {

    @ExceptionHandler(MessageEmptyException::class)
    fun messageEmpty(messageEmptyException: MessageEmptyException
    ) : ResponseEntity<ResponseMapper> {
        val res = ResponseMapper(HttpStatus.BAD_REQUEST.value(),
            ResponseConstants.MESSAGE_EMPTY.value, messageEmptyException.message)
        return ResponseEntity.unprocessableEntity().body(res)
    }

    @ExceptionHandler(MessageRecipientInvalidException::class)
    fun messageRecipientInvalid(messageRecipientInvalidException: MessageRecipientInvalidException
    ) : ResponseEntity<ResponseMapper> {
        val res = ResponseMapper(HttpStatus.NOT_FOUND.value(),
            ResponseConstants.MESSAGE_RECIPIENT_INVALID.value,
            messageRecipientInvalidException.message)
        return ResponseEntity.unprocessableEntity().body(res)
    }
}