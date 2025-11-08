package com.example.projectedu.ui.screens.auth

data class RecoveryState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val recoverySuccess: Boolean = false,
    val errorMessage: String? = null
)