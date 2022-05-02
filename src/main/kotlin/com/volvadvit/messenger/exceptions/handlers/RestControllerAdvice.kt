package com.volvadvit.messenger.exceptions.handlers

import com.volvadvit.messenger.api.v1.models.ResponseMapper
import com.volvadvit.messenger.constants.ResponseConstants
import com.volvadvit.messenger.exceptions.UserDeactivatedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestControllerAdvice {

    @ExceptionHandler(UserDeactivatedException::class)
    fun usernameUnavailable(userDeactivatedException: UserDeactivatedException
    ) : ResponseEntity<ResponseMapper> {
        val res = ResponseMapper(
            HttpStatus.NOT_FOUND.value(),
            ResponseConstants.ACCOUNT_DEACTIVATED.value,
            userDeactivatedException.message)
        return ResponseEntity(res, HttpStatus.UNAUTHORIZED) // http 403 error+
    }
}