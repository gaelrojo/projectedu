package com.example.projectedu.ui.screens.home

import com.example.projectedu.data.model.Task
import com.example.projectedu.data.model.Subject
import com.example.projectedu.data.model.User

data class HomeState(
    val user: User = User(
        name = "Alex Roldan",
        email = "alex@upp.edu.mx",
        currentLevel = 5,
        currentXP = 350,
        totalXP = 850,
        currentStreak = 7,
        badges = listOf("first_task", "perfect_week", "early_bird")
    ),
    val upcomingTasks: List<Task> = emptyList(),
    val recentSubjects: List<Subject> = emptyList(),
    val weekProgress: Float = 0.67f, // 67%
    val completedTasksThisWeek: Int = 12,
    val totalTasksThisWeek: Int = 18,
    val isLoading: Boolean = false
)