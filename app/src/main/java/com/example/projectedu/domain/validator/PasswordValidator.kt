package com.example.projectedu.domain.validator

import com.example.projectedu.util.Constants

object PasswordValidator {

    fun validate(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La contraseña no puede estar vacía"
            )
        }

        if (password.length < Constants.MIN_PASSWORD_LENGTH) {
            return ValidationResult(
                isValid = false,
                errorMessage = Constants.ErrorMessages.INVALID_PASSWORD
            )
        }

        if (password.length > Constants.MAX_PASSWORD_LENGTH) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La contraseña no puede exceder ${Constants.MAX_PASSWORD_LENGTH} caracteres"
            )
        }

        return ValidationResult(isValid = true)
    }

    fun validateStrong(password: String): ValidationResult {
        val basicValidation = validate(password)
        if (!basicValidation.isValid) {
            return basicValidation
        }

        if (!password.any { it.isLetter() }) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La contraseña debe contener al menos una letra"
            )
        }

        if (!password.any { it.isDigit() }) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La contraseña debe contener al menos un número"
            )
        }

        return ValidationResult(isValid = true)
    }

    fun validateMatch(password: String, confirmPassword: String): ValidationResult {
        if (password != confirmPassword) {
            return ValidationResult(
                isValid = false,
                errorMessage = Constants.ErrorMessages.PASSWORDS_DONT_MATCH
            )
        }
        return ValidationResult(isValid = true)
    }

    fun calculateStrength(password: String): Int {
        var strength = 0

        when {
            password.length >= 12 -> strength += 30
            password.length >= 8 -> strength += 20
            password.length >= 6 -> strength += 10
        }

        if (password.any { it.isLowerCase() }) strength += 10
        if (password.any { it.isUpperCase() }) strength += 20
        if (password.any { it.isDigit() }) strength += 20
        if (password.any { !it.isLetterOrDigit() }) strength += 20

        return strength.coerceIn(0, 100)
    }

    fun isValid(password: String): Boolean = validate(password).isValid
}