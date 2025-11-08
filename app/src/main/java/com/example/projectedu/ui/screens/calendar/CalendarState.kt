package com.example.projectedu.ui.screens.calendar

import com.example.projectedu.data.model.Task
import java.util.*

data class CalendarState(
    val selectedDate: Date = Date(),
    val currentMonth: Date = Date(),
    val tasksForSelectedDate: List<Task> = emptyList(),
    val allTasks: List<Task> = emptyList(),
    val isLoading: Boolean = false
)