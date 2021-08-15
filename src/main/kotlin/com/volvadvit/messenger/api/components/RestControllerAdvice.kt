package com.volvadvit.messenger.api.components

import com.volvadvit.messenger.api.constants.ErrorResponse
import com.volvadvit.messenger.api.constants.ResponseConstants
import com.volvadvit.messenger.api.exceptions.UserDeactivatedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestControllerAdvice {

    @ExceptionHandler(UserDeactivatedException::class)
    fun usernameUnavailable(userDeactivatedException: UserDeactivatedException
    ) : ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(ResponseConstants.ACCOUNT_DEACTIVATED.value, userDeactivatedException.message)
        return ResponseEntity(res, HttpStatus.UNAUTHORIZED) // http 403 error+
    }
}