package com.example.projectedu.ui.screens.subjects

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectedu.data.model.Subject
import com.example.projectedu.data.repository.AuthRepository
import java.util.UUID
import kotlinx.coroutines.launch

class SubjectsViewModel : ViewModel() {

    private val _state = mutableStateOf(value = SubjectsState())
    val state: State<SubjectsState> = _state

    private val authRepository = AuthRepository()

    init {
        loadSubjects()
    }

    private fun loadSubjects() {
        val currentUser = authRepository.getCurrentUser()
        if (currentUser != null) {
            _state.value = _state.value.copy(
                subjects = getDummySubjects(userId = currentUser.id),
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
        val currentSubjects = _state.value.subjects.toMutableList()
        currentSubjects.add(subject)
        _state.value = _state.value.copy(subjects = currentSubjects)
        hideDialog()
    }

    fun updateSubject(subject: Subject) {
        val currentSubjects = _state.value.subjects.toMutableList()
        val index = currentSubjects.indexOfFirst { it.id == subject.id }
        if (index != -1) {
            currentSubjects[index] = subject
            _state.value = _state.value.copy(subjects = currentSubjects)
        }
        hideDialog()
    }

    fun deleteSubject(subjectId: String) {
        val currentSubjects = _state.value.subjects.toMutableList()
        currentSubjects.removeAll { it.id == subjectId }
        _state.value = _state.value.copy(subjects = currentSubjects)
    }

    private fun getDummySubjects(userId: String): List<Subject> {
        return listOf(
            Subject(
                id = UUID.randomUUID().toString(),
                userId = userId,
                name = "Matemáticas",
                color = "#FF6B6B",
                professorName = "Dr. García",
                classroom = "Aula 101",
                schedule = "Lun-Mier 8:00-10:00"
            ),
            Subject(
                id = UUID.randomUUID().toString(),
                userId = userId,
                name = "Programación",
                color = "#4ECDC4",
                professorName = "Ing. López",
                classroom = "Lab 3",
                schedule = "Mar-Jue 10:00-12:00"
            ),
            Subject(
                id = UUID.randomUUID().toString(),
                userId = userId,
                name = "Física",
                color = "#FFE66D",
                professorName = "Dr. Martínez",
                classroom = "Aula 205",
                schedule = "Lun-Vier 14:00-16:00"
            )
        )
    }
}