package com.example.projectedu.ui.screens.tasks

import com.example.projectedu.data.model.Subtask
import com.example.projectedu.data.model.Task
import java.util.*

data class TasksState(
    val tasks: List<Task> = emptyList(),
    val filteredTasks: List<Task> = emptyList(),
    val selectedFilter: TaskFilter = TaskFilter.ALL,

    // Diálogos
    val showCreateDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val showSubtasksDialog: Boolean = false, // ← NUEVO

    // Tarea seleccionada
    val selectedTask: Task? = null,

    // Campos de edición
    val editTitle: String = "",
    val editDescription: String = "",
    val editNotes: String = "",
    val editSubject: String = "",
    val editPriority: String = "Media",
    val editType: String = "Tarea",
    val editDueDate: Long = System.currentTimeMillis(),
    val editSubtasks: List<Subtask> = emptyList(), // ← NUEVO
    val newSubtaskTitle: String = "", // ← NUEVO

    // Estados
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

enum class TaskFilter {
    ALL, PENDING, COMPLETED, HIGH_PRIORITY
}