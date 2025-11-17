package com.example.projectedu.data.model

import java.util.*

data class Task(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val notes: String = "",
    val subjectId: String = "", // ← ID de la materia
    val subjectName: String = "", // ← Mantener por compatibilidad
    val dueDate: Date = Date(),
    val priority: String = "Media", // Baja, Media, Alta
    val type: String = "Tarea", // Tarea, Examen, Proyecto, Lectura
    val xpReward: Int = 0,
    val isCompleted: Boolean = false,
    val completedDate: Date? = null,
    val subtasks: List<Subtask> = emptyList()
) {
    // Calcular progreso de subtareas
    fun getSubtaskProgress(): Float {
        if (subtasks.isEmpty()) return if (isCompleted) 1f else 0f
        val completed = subtasks.count { it.isCompleted }
        return completed.toFloat() / subtasks.size.toFloat()
    }

    // Verificar si todas las subtareas están completadas
    fun areAllSubtasksCompleted(): Boolean {
        if (subtasks.isEmpty()) return isCompleted
        return subtasks.all { it.isCompleted }
    }

    // Contar subtareas completadas
    fun getCompletedSubtasksCount(): Int {
        return subtasks.count { it.isCompleted }
    }
}