package com.example.projectedu.ui.screens.calendar

import androidx.lifecycle.ViewModel
import com.example.projectedu.data.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class CalendarViewModel : ViewModel() {

    private val _state = MutableStateFlow(CalendarState())
    val state: StateFlow<CalendarState> = _state.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        val mockTasks = listOf(
            Task(
                id = "1",
                title = "Examen de Móviles 2",
                description = "Estudiar Jetpack Compose y MVVM",
                subjectName = "Desarrollo Móvil",
                dueDate = Date(System.currentTimeMillis() + 86400000),
                priority = "Alta",
                type = "Examen",
                xpReward = 50
            ),
            Task(
                id = "2",
                title = "Práctica de Base de Datos",
                description = "Implementar triggers",
                subjectName = "Base de Datos",
                dueDate = Date(System.currentTimeMillis() + 172800000),
                priority = "Media",
                type = "Tarea",
                xpReward = 25
            ),
            Task(
                id = "3",
                title = "Proyecto Final",
                description = "App móvil completa",
                subjectName = "Desarrollo Móvil",
                dueDate = Date(System.currentTimeMillis() + 2592000000),
                priority = "Alta",
                type = "Proyecto",
                xpReward = 100
            )
        )

        _state.value = _state.value.copy(
            allTasks = mockTasks,
            tasksForSelectedDate = getTasksForDate(mockTasks, Date())
        )
    }

    fun onDateSelected(date: Date) {
        val tasksForDate = getTasksForDate(_state.value.allTasks, date)
        _state.value = _state.value.copy(
            selectedDate = date,
            tasksForSelectedDate = tasksForDate
        )
    }

    fun onMonthChange(month: Date) {
        _state.value = _state.value.copy(currentMonth = month)
    }

    private fun getTasksForDate(tasks: List<Task>, date: Date): List<Task> {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val targetDay = calendar.get(Calendar.DAY_OF_YEAR)
        val targetYear = calendar.get(Calendar.YEAR)

        return tasks.filter { task ->
            calendar.time = task.dueDate
            val taskDay = calendar.get(Calendar.DAY_OF_YEAR)
            val taskYear = calendar.get(Calendar.YEAR)
            taskDay == targetDay && taskYear == targetYear
        }
    }

    fun getTasksForMonth(month: Date): Map<Int, Int> {
        val calendar = Calendar.getInstance()
        calendar.time = month
        val targetMonth = calendar.get(Calendar.MONTH)
        val targetYear = calendar.get(Calendar.YEAR)

        val tasksPerDay = mutableMapOf<Int, Int>()

        _state.value.allTasks.forEach { task ->
            calendar.time = task.dueDate
            if (calendar.get(Calendar.MONTH) == targetMonth &&
                calendar.get(Calendar.YEAR) == targetYear) {
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                tasksPerDay[day] = (tasksPerDay[day] ?: 0) + 1
            }
        }

        return tasksPerDay
    }
}