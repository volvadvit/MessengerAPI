package com.volvadvit.messenger.exceptions.handlers

import com.volvadvit.messenger.api.v1.models.ResponseMapper
import com.volvadvit.messenger.constants.ResponseConstants
import com.volvadvit.messenger.exceptions.InvalidUserIdException
import com.volvadvit.messenger.exceptions.UsernameUnavailableException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class UserControllerAdvice {

    @ExceptionHandler(UsernameUnavailableException::class)
    fun usernameUnavailable(usernameUnavailableException:
                            UsernameUnavailableException) : ResponseEntity<ResponseMapper> {
        val res = ResponseMapper(
            HttpStatus.NOT_FOUND.value(),
            ResponseConstants.USERNAME_UNAVAILABLE.value,
            usernameUnavailableException.message)
        return ResponseEntity.unprocessableEntity().body(res)
    }

    @ExceptionHandler(InvalidUserIdException::class)
    fun invalidId(invalidUserIdException: InvalidUserIdException) : ResponseEntity<ResponseMapper> {
        val res = ResponseMapper(
            HttpStatus.NOT_FOUND.value(),
            ResponseConstants.INVALID_USER_ID.value,
            invalidUserIdException.message)
        return ResponseEntity.badRequest().body(res)
    }
}