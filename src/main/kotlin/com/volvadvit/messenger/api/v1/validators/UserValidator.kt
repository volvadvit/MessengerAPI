package com.volvadvit.messenger.api.v1.validators

import com.volvadvit.messenger.models.User
import org.springframework.stereotype.Component
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Validation

@Component
class UserValidator {

    fun validate(user: User?): User? {
        val validator = Validation.buildDefaultValidatorFactory().validator
        val violations: Set<ConstraintViolation<User>> = validator.validate(user)
        return if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        } else {
            user
        }
    }
}