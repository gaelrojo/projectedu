package com.example.projectedu.ui.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectedu.data.model.Task
import com.example.projectedu.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class TasksViewModel : ViewModel() {

    private val _state = MutableStateFlow(TasksState())
    val state: StateFlow<TasksState> = _state.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        // Datos mock (después conectarás con base de datos real)
        val mockTasks = listOf(
            Task(
                id = "1",
                title = "Examen de Móviles 2",
                description = "Estudiar Jetpack Compose, MVVM, Navigation",
                subjectId = "1",
                subjectName = "Desarrollo Móvil",
                dueDate = Date(System.currentTimeMillis() + 86400000),
                priority = "Alta",
                type = "Examen",
                xpReward = 50,
                isCompleted = false
            ),
            Task(
                id = "2",
                title = "Práctica de Base de Datos",
                description = "Implementar triggers y procedimientos almacenados",
                subjectId = "2",
                subjectName = "Base de Datos",
                dueDate = Date(System.currentTimeMillis() + 172800000),
                priority = "Media",
                type = "Tarea",
                xpReward = 25,
                isCompleted = false
            ),
            Task(
                id = "3",
                title = "Lectura Capítulo 5",
                description = "Patrones de diseño en ingeniería de software",
                subjectId = "3",
                subjectName = "Ingeniería de Software",
                dueDate = Date(System.currentTimeMillis() + 259200000),
                priority = "Baja",
                type = "Lectura",
                xpReward = 10,
                isCompleted = true,
                completedDate = Date()
            ),
            Task(
                id = "4",
                title = "Proyecto Final",
                description = "Desarrollar app móvil completa con gamificación",
                subjectId = "1",
                subjectName = "Desarrollo Móvil",
                dueDate = Date(System.currentTimeMillis() + 2592000000),
                priority = "Alta",
                type = "Proyecto",
                xpReward = 100,
                isCompleted = false
            )
        )

        _state.value = _state.value.copy(
            tasks = mockTasks,
            filteredTasks = mockTasks
        )
    }

    fun onFilterChange(filter: TaskFilter) {
        val filtered = when (filter) {
            TaskFilter.ALL -> _state.value.tasks
            TaskFilter.PENDING -> _state.value.tasks.filter { !it.isCompleted }
            TaskFilter.COMPLETED -> _state.value.tasks.filter { it.isCompleted }
            TaskFilter.HIGH_PRIORITY -> _state.value.tasks.filter { it.priority == "Alta" && !it.isCompleted }
        }

        _state.value = _state.value.copy(
            selectedFilter = filter,
            filteredTasks = filtered
        )
    }

    fun onShowCreateDialog() {
        _state.value = _state.value.copy(
            showCreateDialog = true,
            editTitle = "",
            editDescription = "",
            editSubject = "",
            editPriority = "Media",
            editType = "Tarea",
            editDueDate = System.currentTimeMillis()
        )
    }

    fun onShowEditDialog(task: Task) {
        _state.value = _state.value.copy(
            showEditDialog = true,
            selectedTask = task,
            editTitle = task.title,
            editDescription = task.description,
            editSubject = task.subjectName,
            editPriority = task.priority,
            editType = task.type,
            editDueDate = task.dueDate.time
        )
    }

    fun onShowDeleteDialog(task: Task) {
        _state.value = _state.value.copy(
            showDeleteDialog = true,
            selectedTask = task
        )
    }

    fun onDismissDialogs() {
        _state.value = _state.value.copy(
            showCreateDialog = false,
            showEditDialog = false,
            showDeleteDialog = false,
            selectedTask = null
        )
    }

    fun onTitleChange(title: String) {
        _state.value = _state.value.copy(editTitle = title)
    }

    fun onDescriptionChange(description: String) {
        _state.value = _state.value.copy(editDescription = description)
    }

    fun onSubjectChange(subject: String) {
        _state.value = _state.value.copy(editSubject = subject)
    }

    fun onPriorityChange(priority: String) {
        _state.value = _state.value.copy(editPriority = priority)
    }

    fun onTypeChange(type: String) {
        _state.value = _state.value.copy(editType = type)
    }

    fun onDueDateChange(date: Long) {
        _state.value = _state.value.copy(editDueDate = date)
    }

    fun onCreateTask() {
        if (_state.value.editTitle.isBlank()) {
            _state.value = _state.value.copy(
                errorMessage = Constants.ErrorMessages.EMPTY_FIELD
            )
            return
        }

        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            delay(1000)

            val xpReward = when (_state.value.editPriority) {
                "Alta" -> Constants.XP_HIGH_PRIORITY
                "Media" -> Constants.XP_MEDIUM_PRIORITY
                else -> Constants.XP_LOW_PRIORITY
            }

            val newTask = Task(
                id = UUID.randomUUID().toString(),
                title = _state.value.editTitle,
                description = _state.value.editDescription,
                subjectName = _state.value.editSubject,
                dueDate = Date(_state.value.editDueDate),
                priority = _state.value.editPriority,
                type = _state.value.editType,
                xpReward = xpReward,
                isCompleted = false
            )

            val updatedTasks = _state.value.tasks + newTask

            _state.value = _state.value.copy(
                tasks = updatedTasks,
                filteredTasks = updatedTasks,
                isLoading = false,
                showCreateDialog = false,
                successMessage = Constants.SuccessMessages.TASK_CREATED
            )

            delay(2000)
            _state.value = _state.value.copy(successMessage = null)
        }
    }

    fun onUpdateTask() {
        if (_state.value.editTitle.isBlank()) {
            _state.value = _state.value.copy(
                errorMessage = Constants.ErrorMessages.EMPTY_FIELD
            )
            return
        }

        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            delay(1000)

            val updatedTasks = _state.value.tasks.map { task ->
                if (task.id == _state.value.selectedTask?.id) {
                    task.copy(
                        title = _state.value.editTitle,
                        description = _state.value.editDescription,
                        subjectName = _state.value.editSubject,
                        priority = _state.value.editPriority,
                        type = _state.value.editType,
                        dueDate = Date(_state.value.editDueDate)
                    )
                } else task
            }

            _state.value = _state.value.copy(
                tasks = updatedTasks,
                filteredTasks = updatedTasks,
                isLoading = false,
                showEditDialog = false,
                successMessage = "Tarea actualizada"
            )

            delay(2000)
            _state.value = _state.value.copy(successMessage = null)
        }
    }

    fun onDeleteTask() {
        viewModelScope.launch {
            val updatedTasks = _state.value.tasks.filter { it.id != _state.value.selectedTask?.id }

            _state.value = _state.value.copy(
                tasks = updatedTasks,
                filteredTasks = updatedTasks,
                showDeleteDialog = false,
                successMessage = Constants.SuccessMessages.TASK_DELETED
            )

            delay(2000)
            _state.value = _state.value.copy(successMessage = null)
        }
    }

    fun onToggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTasks = _state.value.tasks.map {
                if (it.id == task.id) {
                    it.copy(
                        isCompleted = !it.isCompleted,
                        completedDate = if (!it.isCompleted) Date() else null
                    )
                } else it
            }

            _state.value = _state.value.copy(
                tasks = updatedTasks,
                filteredTasks = updatedTasks,
                successMessage = if (!task.isCompleted) {
                    String.format(Constants.SuccessMessages.TASK_COMPLETED, task.xpReward)
                } else {
                    "Tarea marcada como pendiente"
                }
            )

            delay(2000)
            _state.value = _state.value.copy(successMessage = null)
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}