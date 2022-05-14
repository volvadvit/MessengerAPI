package com.volvadvit.messenger.exceptions.handlers

import com.volvadvit.messenger.api.v1.models.ResponseMapper
import com.volvadvit.messenger.constants.ResponseConstants
import com.volvadvit.messenger.exceptions.InvalidUserIdException
import com.volvadvit.messenger.exceptions.UserDeactivatedException
import com.volvadvit.messenger.exceptions.UsernameUnavailableException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.Timestamp
import java.time.Instant

@RestControllerAdvice
class UserControllerAdvice {

    @ExceptionHandler(UsernameUnavailableException::class)
    fun usernameUnavailable(usernameUnavailableException:
                            UsernameUnavailableException) : ResponseEntity<ResponseMapper> {
        val res = ResponseMapper(
            HttpStatus.NOT_FOUND.value(),
            ResponseConstants.USERNAME_UNAVAILABLE.value,
            usernameUnavailableException.message,
            Timestamp.from(Instant.now()))
        return ResponseEntity.unprocessableEntity().body(res)
    }

    @ExceptionHandler(InvalidUserIdException::class)
    fun invalidId(invalidUserIdException: InvalidUserIdException) : ResponseEntity<ResponseMapper> {
        val res = ResponseMapper(
            HttpStatus.NOT_FOUND.value(),
            ResponseConstants.ID_NULL.value,
            invalidUserIdException.message,
            Timestamp.from(Instant.now()))
        return ResponseEntity.badRequest().body(res)
    }

    @ExceptionHandler(UserDeactivatedException::class)
    fun userDeactivated(userDeactivatedException: UserDeactivatedException
    ) : ResponseEntity<ResponseMapper> {
        val res = ResponseMapper(
            HttpStatus.FORBIDDEN.value(),
            ResponseConstants.ACCOUNT_DEACTIVATED.value,
            userDeactivatedException.message,
            Timestamp.from(Instant.now()))
        return ResponseEntity(res, HttpStatus.FORBIDDEN)
    }
}