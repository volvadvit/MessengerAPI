package com.volvadvit.messenger.exceptions.handlers

import com.volvadvit.messenger.api.v1.models.ResponseMapper
import com.volvadvit.messenger.constants.ResponseConstants
import com.volvadvit.messenger.exceptions.InvalidTokenException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant
import java.util.*

@ControllerAdvice
class AuthControllerAdvice {

    @ExceptionHandler(InvalidTokenException::class)
    fun invalidToken(ex: InvalidTokenException
    ) : ResponseEntity<ResponseMapper> {
        val res = ResponseMapper(
            HttpStatus.FORBIDDEN.value(),
            ResponseConstants.INVALID_TOKEN.value,
            ex.message,
            Date.from(Instant.now()))
        return ResponseEntity(res, HttpStatus.FORBIDDEN)
    }
}