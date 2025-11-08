package com.example.projectedu.domain.validator

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)