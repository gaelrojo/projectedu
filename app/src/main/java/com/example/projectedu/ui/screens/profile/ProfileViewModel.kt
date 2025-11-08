package com.example.projectedu.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectedu.data.model.User
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
        // Datos del usuario actual (después vendrá de Firebase)
        val currentUser = User(
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

        _state.value = _state.value.copy(
            user = currentUser,
            editName = currentUser.name,
            editUniversity = currentUser.university,
            editCareer = currentUser.career,
            editAge = currentUser.age.toString()
        )
    }

    fun onEditClick() {
        _state.value = _state.value.copy(isEditing = true)
    }

    fun onCancelEdit() {
        _state.value = _state.value.copy(
            isEditing = false,
            editName = _state.value.user.name,
            editUniversity = _state.value.user.university,
            editCareer = _state.value.user.career,
            editAge = _state.value.user.age.toString()
        )
    }

    fun onNameChange(name: String) {
        _state.value = _state.value.copy(editName = name)
    }

    fun onUniversityChange(university: String) {
        _state.value = _state.value.copy(editUniversity = university)
    }

    fun onCareerChange(career: String) {
        _state.value = _state.value.copy(editCareer = career)
    }

    fun onAgeChange(age: String) {
        if (age.isEmpty() || age.all { it.isDigit() }) {
            _state.value = _state.value.copy(editAge = age)
        }
    }

    fun onSaveClick() {
        if (_state.value.editName.isBlank()) {
            _state.value = _state.value.copy(
                errorMessage = "El nombre no puede estar vacío"
            )
            return
        }

        _state.value = _state.value.copy(isSaving = true)

        viewModelScope.launch {
            delay(1500)

            val updatedUser = _state.value.user.copy(
                name = _state.value.editName,
                university = _state.value.editUniversity,
                career = _state.value.editCareer,
                age = _state.value.editAge.toIntOrNull() ?: 21
            )

            _state.value = _state.value.copy(
                user = updatedUser,
                isEditing = false,
                isSaving = false,
                saveSuccess = true
            )

            delay(2000)
            _state.value = _state.value.copy(saveSuccess = false)
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }

    fun calculateXPForNextLevel(): Int {
        val currentLevel = _state.value.user.currentLevel
        return 100 + (currentLevel * 50)
    }

    fun getXPProgress(): Float {
        val currentXP = _state.value.user.currentXP
        val xpForNext = calculateXPForNextLevel()
        return (currentXP.toFloat() / xpForNext).coerceIn(0f, 1f)
    }
}