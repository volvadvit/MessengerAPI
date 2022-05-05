package com.volvadvit.messenger.api.v1.validators

import com.volvadvit.messenger.models.Message
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Validation

@org.springframework.stereotype.Component
class MessageValidator {

    fun validate(message: Message?): Message? {
        val validator = Validation.buildDefaultValidatorFactory().validator
        val violations: Set<ConstraintViolation<Message>> = validator.validate(message)
        return if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        } else {
            message
        }
    }
}