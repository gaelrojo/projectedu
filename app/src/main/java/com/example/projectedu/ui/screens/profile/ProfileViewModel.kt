package com.example.projectedu.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectedu.data.model.User
import com.example.projectedu.domain.validator.NameValidator
import com.example.projectedu.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        // Datos mock (después conectarás con base de datos real)
        val mockUser = User(
            id = "1",
            name = "Alex Roldan",
            email = "alex@upp.edu.mx",
            university = "Universidad Politécnica de Pachuca",
            career = "Ingeniería en Software",
            age = 21,
            currentLevel = 5,
            currentXP = 350,
            totalXP = 850,
            currentStreak = 7,
            badges = listOf("first_task", "perfect_week", "early_bird")
        )

        _state.value = _state.value.copy(user = mockUser)
    }

    fun onEditClick() {
        _state.value = _state.value.copy(
            isEditing = true,
            editName = _state.value.user.name,
            editUniversity = _state.value.user.university,
            editCareer = _state.value.user.career,
            editAge = _state.value.user.age.toString()
        )
    }

    fun onNameChange(name: String) {
        // Filtrar caracteres no permitidos en tiempo real
        val filteredName = name.filter { char ->
            char.isLetter() || char.isWhitespace()
        }.take(30) // Limitar a 30 caracteres

        _state.value = _state.value.copy(
            editName = filteredName,
            nameError = null
        )
    }

    fun onUniversityChange(university: String) {
        _state.value = _state.value.copy(editUniversity = university)
    }

    fun onCareerChange(career: String) {
        _state.value = _state.value.copy(editCareer = career)
    }

    fun onAgeChange(age: String) {
        // Solo permitir números
        val filteredAge = age.filter { it.isDigit() }.take(2)
        _state.value = _state.value.copy(editAge = filteredAge)
    }

    fun onSaveClick() {
        // Limpiar errores
        _state.value = _state.value.copy(
            nameError = null,
            errorMessage = null
        )

        var hasError = false

        // Validar nombre con el nuevo validador
        val nameValidation = NameValidator.validate(_state.value.editName)
        if (!nameValidation.isValid) {
            _state.value = _state.value.copy(
                nameError = nameValidation.errorMessage
            )
            hasError = true
        }

        // Validar universidad
        if (_state.value.editUniversity.isBlank()) {
            _state.value = _state.value.copy(
                errorMessage = "La universidad no puede estar vacía"
            )
            hasError = true
        }

        // Validar carrera
        if (_state.value.editCareer.isBlank()) {
            _state.value = _state.value.copy(
                errorMessage = "La carrera no puede estar vacía"
            )
            hasError = true
        }

        // Validar edad
        val age = _state.value.editAge.toIntOrNull()
        if (age == null || age < 16 || age > 99) {
            _state.value = _state.value.copy(
                errorMessage = "La edad debe estar entre 16 y 99 años"
            )
            hasError = true
        }

        if (hasError) return

        // Guardar cambios
        _state.value = _state.value.copy(isSaving = true)

        viewModelScope.launch {
            delay(2000)

            val updatedUser = _state.value.user.copy(
                name = _state.value.editName,
                university = _state.value.editUniversity,
                career = _state.value.editCareer,
                age = age!!
            )

            _state.value = _state.value.copy(
                user = updatedUser,
                isEditing = false,
                isSaving = false,
                saveSuccess = true
            )

            delay(3000)
            _state.value = _state.value.copy(saveSuccess = false)
        }
    }

    fun onCancelEdit() {
        _state.value = _state.value.copy(
            isEditing = false,
            nameError = null,
            errorMessage = null
        )
    }

    fun calculateXPForNextLevel(): Int {
        val currentLevel = _state.value.user.currentLevel
        return Constants.XP_BASE_PER_LEVEL + (currentLevel * Constants.XP_LEVEL_INCREMENT)
    }

    fun getXPProgress(): Float {
        val currentXP = _state.value.user.currentXP
        val xpForNext = calculateXPForNextLevel()
        return (currentXP.toFloat() / xpForNext).coerceIn(0f, 1f)
    }
}