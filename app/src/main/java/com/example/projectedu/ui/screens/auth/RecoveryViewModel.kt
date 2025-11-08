package com.example.projectedu.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectedu.domain.validator.EmailValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecoveryViewModel : ViewModel() {

    private val _state = MutableStateFlow(RecoveryState())
    val state: StateFlow<RecoveryState> = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(
            email = email,
            emailError = null,
            errorMessage = null
        )
    }

    fun onRecoverClick() {
        _state.value = _state.value.copy(
            emailError = null,
            errorMessage = null
        )

        val emailValidation = EmailValidator.validate(_state.value.email)
        if (!emailValidation.isValid) {
            _state.value = _state.value.copy(
                emailError = emailValidation.errorMessage
            )
            return
        }

        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            delay(2000)
            _state.value = _state.value.copy(
                isLoading = false,
                recoverySuccess = true
            )
        }
    }

    fun clearSuccess() {
        _state.value = _state.value.copy(recoverySuccess = false)
    }
}