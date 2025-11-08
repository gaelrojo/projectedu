package com.example.projectedu.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectedu.domain.validator.EmailValidator
import com.example.projectedu.domain.validator.PasswordValidator
import com.example.projectedu.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun onNameChange(name: String) {
        _state.value = _state.value.copy(
            name = name,
            nameError = null
        )
    }

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(
            email = email,
            emailError = null
        )
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(
            password = password,
            passwordError = null
        )
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _state.value = _state.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = null
        )
    }

    fun onRegisterClick() {
        // Limpiar errores
        _state.value = _state.value.copy(
            nameError = null,
            emailError = null,
            passwordError = null,
            confirmPasswordError = null,
            errorMessage = null
        )

        var hasError = false

        // Validar nombre
        if (_state.value.name.isBlank()) {
            _state.value = _state.value.copy(
                nameError = Constants.ErrorMessages.EMPTY_FIELD
            )
            hasError = true
        } else if (_state.value.name.length < Constants.MIN_NAME_LENGTH) {
            _state.value = _state.value.copy(
                nameError = Constants.ErrorMessages.INVALID_NAME
            )
            hasError = true
        }

        // Validar email
        val emailValidation = EmailValidator.validate(_state.value.email)
        if (!emailValidation.isValid) {
            _state.value = _state.value.copy(
                emailError = emailValidation.errorMessage
            )
            hasError = true
        }

        // Validar contraseña
        val passwordValidation = PasswordValidator.validateStrong(_state.value.password)
        if (!passwordValidation.isValid) {
            _state.value = _state.value.copy(
                passwordError = passwordValidation.errorMessage
            )
            hasError = true
        }

        // Validar confirmación
        val matchValidation = PasswordValidator.validateMatch(
            _state.value.password,
            _state.value.confirmPassword
        )
        if (!matchValidation.isValid) {
            _state.value = _state.value.copy(
                confirmPasswordError = matchValidation.errorMessage
            )
            hasError = true
        }

        if (hasError) return

        // Iniciar registro
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            delay(2000)
            _state.value = _state.value.copy(
                isLoading = false,
                registerSuccess = true
            )
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}