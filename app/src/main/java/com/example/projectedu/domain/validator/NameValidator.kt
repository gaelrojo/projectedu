package com.example.projectedu.domain.validator

import com.example.projectedu.util.Constants

object NameValidator {

    fun validate(name: String): ValidationResult {
        // Verificar si está vacío
        if (name.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre no puede estar vacío"
            )
        }

        // Verificar longitud mínima
        if (name.trim().length < Constants.MIN_NAME_LENGTH) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre debe tener al menos ${Constants.MIN_NAME_LENGTH} caracteres"
            )
        }

        // Verificar longitud máxima (30 caracteres)
        if (name.trim().length > 30) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre no puede exceder 30 caracteres"
            )
        }

        // Verificar que solo contenga letras y espacios
        val namePattern = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")
        if (!namePattern.matches(name.trim())) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre solo puede contener letras y espacios"
            )
        }

        // Verificar que no contenga números
        if (name.any { it.isDigit() }) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre no puede contener números"
            )
        }

        // Verificar que no contenga caracteres especiales (excepto espacios)
        val specialCharsPattern = Regex("[!@#\$%^&*()_+=\\[\\]{};':\"\\\\|,.<>/?`~]")
        if (specialCharsPattern.containsMatchIn(name)) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre no puede contener caracteres especiales"
            )
        }

        // Verificar que no contenga emojis
        val emojiPattern = Regex("[\\p{So}\\p{Sk}]")
        if (emojiPattern.containsMatchIn(name)) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre no puede contener emojis"
            )
        }

        // Verificar que tenga al menos una letra
        if (!name.any { it.isLetter() }) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre debe contener al menos una letra"
            )
        }

        // Verificar que no tenga múltiples espacios consecutivos
        if (name.contains("  ")) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre no puede tener espacios consecutivos"
            )
        }

        return ValidationResult(isValid = true)
    }

    fun isValid(name: String): Boolean = validate(name).isValid
}