package com.example.projectedu.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.projectedu.data.model.Subject
import com.example.projectedu.data.model.Task
import com.example.projectedu.ui.theme.SubjectColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        // Datos de ejemplo (después conectarás con una base de datos real)
        val mockTasks = listOf(
            Task(
                id = "1",
                title = "Examen de Móviles 2",
                description = "Estudiar Jetpack Compose y arquitectura MVVM",
                subjectName = "Desarrollo Móvil",
                dueDate = Date(System.currentTimeMillis() + 86400000), // Mañana
                priority = "Alta",
                type = "Examen",
                xpReward = 50
            ),
            Task(
                id = "2",
                title = "Práctica de Base de Datos",
                description = "Implementar triggers y procedimientos",
                subjectName = "Base de Datos",
                dueDate = Date(System.currentTimeMillis() + 172800000), // 2 días
                priority = "Media",
                type = "Tarea",
                xpReward = 25
            ),
            Task(
                id = "3",
                title = "Lectura Capítulo 5",
                description = "Leer sobre patrones de diseño",
                subjectName = "Ingeniería de Software",
                dueDate = Date(System.currentTimeMillis() + 259200000), // 3 días
                priority = "Baja",
                type = "Lectura",
                xpReward = 10
            )
        )

        val mockSubjects = listOf(
            Subject(
                id = "1",
                name = "Móviles 2",
                color = SubjectColors[0].value.toLong(),
                teacher = "Miguel Angel Montoya",
                totalTasks = 10,
                completedTasks = 7
            ),
            Subject(
                id = "2",
                name = "Base de Datos",
                color = SubjectColors[1].value.toLong(),
                teacher = "Gael Rojo",
                totalTasks = 8,
                completedTasks = 5
            ),
            Subject(
                id = "3",
                name = "Ing. Software",
                color = SubjectColors[2].value.toLong(),
                teacher = "Profesor X",
                totalTasks = 12,
                completedTasks = 10
            )
        )

        _state.value = _state.value.copy(
            upcomingTasks = mockTasks,
            recentSubjects = mockSubjects
        )
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