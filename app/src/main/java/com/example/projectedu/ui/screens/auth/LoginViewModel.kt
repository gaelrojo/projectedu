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

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    // Credenciales de prueba (hardcoded)
    private val testEmail = "alex@upp.edu.mx"
    private val testPassword = "123456"

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(
            email = email,
            emailError = null,
            errorMessage = null
        )
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(
            password = password,
            passwordError = null,
            errorMessage = null
        )
    }

    fun onLoginClick() {
        // Limpiar errores previos
        _state.value = _state.value.copy(
            emailError = null,
            passwordError = null,
            errorMessage = null
        )

        // Validar email
        val emailValidation = EmailValidator.validate(_state.value.email)
        if (!emailValidation.isValid) {
            _state.value = _state.value.copy(
                emailError = emailValidation.errorMessage
            )
            return
        }

        // Validar password
        val passwordValidation = PasswordValidator.validate(_state.value.password)
        if (!passwordValidation.isValid) {
            _state.value = _state.value.copy(
                passwordError = passwordValidation.errorMessage
            )
            return
        }

        // Iniciar proceso de login
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            // Simular llamada a API (2 segundos)
            delay(2000)

            // Verificar credenciales
            if (_state.value.email == testEmail && _state.value.password == testPassword) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    loginSuccess = true
                )
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = Constants.ErrorMessages.LOGIN_FAILED
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}