package com.example.projectedu.ui.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectedu.data.model.Subtask
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
        val mockTasks = listOf(
            Task(
                id = "1",
                title = "Examen de Móviles 2",
                description = "Estudiar Jetpack Compose, MVVM, Navigation",
                notes = "Repasar los ejercicios del capítulo 5 y 6",
                subjectId = "1",
                subjectName = "Desarrollo Móvil",
                dueDate = Date(System.currentTimeMillis() + 86400000), // Mañana
                priority = "Alta",
                type = "Examen",
                xpReward = 50,
                isCompleted = false,
                subtasks = listOf(
                    Subtask("1", "Estudiar Jetpack Compose", true),
                    Subtask("2", "Repasar MVVM", false),
                    Subtask("3", "Practicar Navigation", false),
                    Subtask("4", "Hacer ejercicios", false)
                )
            ),
            Task(
                id = "2",
                title = "Práctica de Base de Datos",
                description = "Implementar triggers y procedimientos almacenados",
                notes = "Usar la base de datos de ejemplo del profesor",
                subjectId = "2",
                subjectName = "Base de Datos",
                dueDate = Date(System.currentTimeMillis() + 172800000), // En 2 días
                priority = "Media",
                type = "Tarea",
                xpReward = 25,
                isCompleted = false,
                subtasks = listOf(
                    Subtask("1", "Crear triggers", false),
                    Subtask("2", "Crear procedimientos", false)
                )
            ),
            Task(
                id = "3",
                title = "Lectura Capítulo 5",
                description = "Patrones de diseño en ingeniería de software",
                notes = "",
                subjectId = "3",
                subjectName = "Ingeniería de Software",
                dueDate = Date(System.currentTimeMillis() + 259200000), // En 3 días
                priority = "Baja",
                type = "Lectura",
                xpReward = 10,
                isCompleted = true,
                completedDate = Date(),
                subtasks = listOf(
                    Subtask("1", "Leer capítulo 5", true),
                    Subtask("2", "Hacer resumen", true)
                )
            ),
            Task(
                id = "4",
                title = "Proyecto Final",
                description = "Desarrollar app móvil completa con gamificación",
                notes = "Incluir: Login, CRUD tareas, gamificación, estadísticas",
                subjectId = "1",
                subjectName = "Desarrollo Móvil",
                dueDate = Date(System.currentTimeMillis() + 2592000000), // En 30 días (fuera de esta semana)
                priority = "Alta",
                type = "Proyecto",
                xpReward = 100,
                isCompleted = false,
                subtasks = listOf(
                    Subtask("1", "Diseñar UI/UX", true),
                    Subtask("2", "Implementar Login", false),
                    Subtask("3", "CRUD de tareas", false),
                    Subtask("4", "Sistema de gamificación", false),
                    Subtask("5", "Estadísticas", false)
                )
            )
        )

        _state.value = _state.value.copy(
            tasks = mockTasks,
            filteredTasks = mockTasks
        )
    }

    // FUNCIONES DE PROGRESO SEMANAL

    private fun getWeeklyTasks(): List<Task> {
        val calendar = Calendar.getInstance()
        val today = calendar.time

        // Obtener el inicio de la semana (Lunes)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val weekStart = calendar.time

        // Obtener el fin de la semana (Domingo)
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val weekEnd = calendar.time

        return _state.value.tasks.filter { task ->
            task.dueDate.after(weekStart) && task.dueDate.before(weekEnd)
        }
    }

    fun getWeeklyTaskCompletionPercentage(): Float {
        val weeklyTasks = getWeeklyTasks()
        if (weeklyTasks.isEmpty()) return 0f
        val completed = weeklyTasks.count { it.isCompleted }
        return completed.toFloat() / weeklyTasks.size.toFloat()
    }

    fun getCompletedWeeklyTasksCount(): Int {
        return getWeeklyTasks().count { it.isCompleted }
    }

    fun getTotalWeeklyTasksCount(): Int {
        return getWeeklyTasks().size
    }

    // FUNCIONES DE SUBTAREAS

    fun onToggleSubtask(task: Task, subtask: Subtask) {
        viewModelScope.launch {
            val updatedSubtasks = task.subtasks.map {
                if (it.id == subtask.id) it.copy(isCompleted = !it.isCompleted)
                else it
            }

            val updatedTasks = _state.value.tasks.map {
                if (it.id == task.id) {
                    val allCompleted = updatedSubtasks.all { sub -> sub.isCompleted }
                    it.copy(
                        subtasks = updatedSubtasks,
                        isCompleted = allCompleted,
                        completedDate = if (allCompleted) Date() else null
                    )
                } else it
            }

            _state.value = _state.value.copy(
                tasks = updatedTasks,
                filteredTasks = updatedTasks
            )
        }
    }

    fun onShowSubtasksDialog(task: Task) {
        _state.value = _state.value.copy(
            showSubtasksDialog = true,
            selectedTask = task,
            editSubtasks = task.subtasks
        )
    }

    fun onNewSubtaskTitleChange(title: String) {
        _state.value = _state.value.copy(newSubtaskTitle = title)
    }

    fun onAddSubtask() {
        if (_state.value.newSubtaskTitle.isBlank()) return

        val newSubtask = Subtask(
            id = UUID.randomUUID().toString(),
            title = _state.value.newSubtaskTitle,
            isCompleted = false
        )

        _state.value = _state.value.copy(
            editSubtasks = _state.value.editSubtasks + newSubtask,
            newSubtaskTitle = ""
        )
    }

    fun onRemoveSubtask(subtask: Subtask) {
        _state.value = _state.value.copy(
            editSubtasks = _state.value.editSubtasks.filter { it.id != subtask.id }
        )
    }

    fun onSaveSubtasks() {
        viewModelScope.launch {
            val updatedTasks = _state.value.tasks.map { task ->
                if (task.id == _state.value.selectedTask?.id) {
                    task.copy(subtasks = _state.value.editSubtasks)
                } else task
            }

            _state.value = _state.value.copy(
                tasks = updatedTasks,
                filteredTasks = updatedTasks,
                showSubtasksDialog = false,
                successMessage = "Subtareas actualizadas"
            )

            delay(3000)
            _state.value = _state.value.copy(successMessage = null)
        }
    }

    // FUNCIONES EXISTENTES (sin cambios)

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
            editNotes = "",
            editSubject = "",
            editPriority = "Media",
            editType = "Tarea",
            editDueDate = System.currentTimeMillis(),
            editSubtasks = emptyList()
        )
    }

    fun onShowEditDialog(task: Task) {
        _state.value = _state.value.copy(
            showEditDialog = true,
            selectedTask = task,
            editTitle = task.title,
            editDescription = task.description,
            editNotes = task.notes,
            editSubject = task.subjectName,
            editPriority = task.priority,
            editType = task.type,
            editDueDate = task.dueDate.time,
            editSubtasks = task.subtasks
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
            showDatePicker = false,
            showTimePicker = false,
            showSubtasksDialog = false,
            selectedTask = null,
            newSubtaskTitle = ""
        )
    }

    fun onTitleChange(title: String) {
        _state.value = _state.value.copy(editTitle = title)
    }

    fun onDescriptionChange(description: String) {
        _state.value = _state.value.copy(editDescription = description)
    }

    fun onNotesChange(notes: String) {
        _state.value = _state.value.copy(editNotes = notes)
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

    fun onShowDatePicker() {
        _state.value = _state.value.copy(showDatePicker = true)
    }

    fun onShowTimePicker() {
        _state.value = _state.value.copy(showTimePicker = true)
    }

    fun onDateSelected(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = _state.value.editDueDate
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        _state.value = _state.value.copy(
            editDueDate = calendar.timeInMillis,
            showDatePicker = false
        )
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = _state.value.editDueDate
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        _state.value = _state.value.copy(
            editDueDate = calendar.timeInMillis,
            showTimePicker = false
        )
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
                notes = _state.value.editNotes,
                subjectName = _state.value.editSubject,
                dueDate = Date(_state.value.editDueDate),
                priority = _state.value.editPriority,
                type = _state.value.editType,
                xpReward = xpReward,
                isCompleted = false,
                subtasks = _state.value.editSubtasks
            )

            val updatedTasks = _state.value.tasks + newTask

            _state.value = _state.value.copy(
                tasks = updatedTasks,
                filteredTasks = updatedTasks,
                isLoading = false,
                showCreateDialog = false,
                successMessage = "\"${_state.value.editTitle}\" creada exitosamente"
            )

            delay(3000)
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
                        notes = _state.value.editNotes,
                        subjectName = _state.value.editSubject,
                        priority = _state.value.editPriority,
                        type = _state.value.editType,
                        dueDate = Date(_state.value.editDueDate),
                        subtasks = _state.value.editSubtasks
                    )
                } else task
            }

            _state.value = _state.value.copy(
                tasks = updatedTasks,
                filteredTasks = updatedTasks,
                isLoading = false,
                showEditDialog = false,
                successMessage = "Tarea actualizada correctamente"
            )

            delay(3000)
            _state.value = _state.value.copy(successMessage = null)
        }
    }

    fun onDeleteTask() {
        viewModelScope.launch {
            val taskTitle = _state.value.selectedTask?.title ?: "Tarea"
            val updatedTasks = _state.value.tasks.filter { it.id != _state.value.selectedTask?.id }

            _state.value = _state.value.copy(
                tasks = updatedTasks,
                filteredTasks = updatedTasks,
                showDeleteDialog = false,
                successMessage = "\"$taskTitle\" eliminada exitosamente"
            )

            delay(3000)
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
                    "¡Tarea completada! +${task.xpReward} XP ganados"
                } else {
                    "Tarea marcada como pendiente"
                }
            )

            delay(3000)
            _state.value = _state.value.copy(successMessage = null)
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}