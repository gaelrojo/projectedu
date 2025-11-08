package com.example.projectedu.data.model

data class Subject(
    val id: String = "",
    val name: String = "",
    val color: Long = 0xFF8B5CF6, // Color en formato hex
    val teacher: String = "",
    val totalTasks: Int = 0,
    val completedTasks: Int = 0
) {
    val progressPercentage: Float
        get() = if (totalTasks > 0) (completedTasks.toFloat() / totalTasks) * 100 else 0f
}