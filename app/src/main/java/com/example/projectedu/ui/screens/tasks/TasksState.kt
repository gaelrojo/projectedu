package com.example.projectedu.ui.screens.tasks

import com.example.projectedu.data.model.Task

data class TasksState(
    val tasks: List<Task> = emptyList(),
    val filteredTasks: List<Task> = emptyList(),
    val selectedFilter: TaskFilter = TaskFilter.ALL,
    val isLoading: Boolean = false,
    val showCreateDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val selectedTask: Task? = null,

    // Campos para crear/editar tarea
    val editTitle: String = "",
    val editDescription: String = "",
    val editSubject: String = "",
    val editPriority: String = "Media",
    val editType: String = "Tarea",
    val editDueDate: Long = System.currentTimeMillis(),

    val errorMessage: String? = null,
    val successMessage: String? = null
)

enum class TaskFilter {
    ALL, PENDING, COMPLETED, HIGH_PRIORITY
}