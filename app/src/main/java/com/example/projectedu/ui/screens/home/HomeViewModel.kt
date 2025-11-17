package com.example.projectedu.ui.screens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.projectedu.data.model.User
import java.util.*

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        // Datos mock del usuario (después conectarás con base de datos real)
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

    // Función para calcular XP necesario para siguiente nivel
    fun calculateXPForNextLevel(): Int {
        val currentLevel = _state.value.user.currentLevel
        return 100 + (currentLevel * 50) // Fórmula: 100 base + 50 por nivel
    }

    // Función para obtener progreso de XP
    fun getXPProgress(): Float {
        val currentXP = _state.value.user.currentXP
        val xpForNext = calculateXPForNextLevel()
        return (currentXP.toFloat() / xpForNext).coerceIn(0f, 1f)
    }
}