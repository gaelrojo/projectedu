package com.example.projectedu.domain.validator

import com.example.projectedu.util.Constants
import java.util.regex.Pattern

object EmailValidator {

    private val emailPattern = Pattern.compile(Constants.EMAIL_REGEX)

    fun validate(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El correo electrónico no puede estar vacío"
            )
        }

        if (email.contains(" ")) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El correo no debe contener espacios"
            )
        }

        if (email.length < 5) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El correo es demasiado corto"
            )
        }

        if (!emailPattern.matcher(email).matches()) {
            return ValidationResult(
                isValid = false,
                errorMessage = Constants.ErrorMessages.INVALID_EMAIL
            )
        }

        if (!email.contains("@") || !email.contains(".")) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El correo debe contener @ y un dominio válido"
            )
        }

        val parts = email.split("@")
        if (parts.size != 2) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El correo solo debe contener un símbolo @"
            )
        }

        return ValidationResult(isValid = true)
    }

    fun isValid(email: String): Boolean = validate(email).isValid
}