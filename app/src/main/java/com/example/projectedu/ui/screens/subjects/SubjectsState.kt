package com.example.projectedu.ui.screens.subjects

import com.example.projectedu.data.model.Subject

data class SubjectsState(
    val subjects: List<Subject> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showAddDialog: Boolean = false,
    val editingSubject: Subject? = null
)