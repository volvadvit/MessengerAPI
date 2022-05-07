package com.volvadvit.messenger.api.v1.validators

import com.volvadvit.messenger.models.Conversation
import org.springframework.stereotype.Component
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Validation

@Component
class ConversationValidator {
    fun validate(conv: Conversation?): Conversation? {
        val validator = Validation.buildDefaultValidatorFactory().validator
        val violations: Set<ConstraintViolation<Conversation>> = validator.validate(conv)
        return if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        } else {
            conv
        }
    }
}