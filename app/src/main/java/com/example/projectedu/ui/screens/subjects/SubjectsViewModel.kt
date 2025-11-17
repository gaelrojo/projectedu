package com.example.projectedu.ui.screens.subjects

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.projectedu.data.model.Subject
import com.example.projectedu.data.repository.AuthRepository
import java.util.UUID

class SubjectsViewModel : ViewModel() {

    private val _state = mutableStateOf(SubjectsState())
    val state: State<SubjectsState> = _state

    private val authRepository = AuthRepository()

    init {
        loadSubjects()
    }

    private fun loadSubjects() {
        val currentUser = authRepository.getCurrentUser()
        if (currentUser != null) {
            _state.value = _state.value.copy(
                subjects = getDummySubjects(currentUser.id),
                isLoading = false
            )
        }
    }

    fun showAddDialog() {
        _state.value = _state.value.copy(
            showAddDialog = true,
            editingSubject = null
        )
    }

    fun showEditDialog(subject: Subject) {
        _state.value = _state.value.copy(
            showAddDialog = true,
            editingSubject = subject
        )
    }

    fun hideDialog() {
        _state.value = _state.value.copy(
            showAddDialog = false,
            editingSubject = null
        )
    }

    fun addSubject(subject: Subject) {
        val currentUser = authRepository.getCurrentUser()
        if (currentUser != null) {
            val newSubject = subject.copy(
                id = UUID.randomUUID().toString(),
                userId = currentUser.id
            )

            val updatedSubjects = _state.value.subjects + newSubject
            _state.value = _state.value.copy(
                subjects = updatedSubjects,
                showAddDialog = false
            )
        }
    }

    fun updateSubject(subject: Subject) {
        val updatedSubjects = _state.value.subjects.map {
            if (it.id == subject.id) subject else it
        }
        _state.value = _state.value.copy(
            subjects = updatedSubjects,
            showAddDialog = false,
            editingSubject = null
        )
    }

    fun deleteSubject(subjectId: String) {
        val updatedSubjects = _state.value.subjects.filter { it.id != subjectId }
        _state.value = _state.value.copy(subjects = updatedSubjects)
    }

    // Datos de ejemplo
    private fun getDummySubjects(userId: String): List<Subject> {
        return listOf(
            Subject(
                id = "1",
                name = "Programación Móvil",
                professor = "Dr. García",
                schedule = "Lun, Mié 08:00-10:00",
                classroom = "Lab 3",
                color = 0xFF8B7FFF,
                userId = userId
            ),
            Subject(
                id = "2",
                name = "Bases de Datos",
                professor = "Ing. Martínez",
                schedule = "Mar, Jue 10:00-12:00",
                classroom = "Aula 201",
                color = 0xFF4A90E2,
                userId = userId
            ),
            Subject(
                id = "3",
                name = "Inteligencia Artificial",
                professor = "Dra. López",
                schedule = "Vie 14:00-17:00",
                classroom = "Lab 5",
                color = 0xFF4CAF50,
                userId = userId
            )
        )
    }
}