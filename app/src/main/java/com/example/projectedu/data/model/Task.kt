package com.example.projectedu.data.model

import java.util.Date
import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val subjectId: String = "",
    val subjectName: String = "",
    val dueDate: Date = Date(),
    val priority: String = "Media", // Baja, Media, Alta
    val type: String = "Tarea", // Examen, Tarea, Proyecto, Lectura, etc.
    val isCompleted: Boolean = false,
    val completedDate: Date? = null,
    val xpReward: Int = 25,
    val createdAt: Date = Date()
)