package com.example.projectedu.ui.screens.profile

import com.example.projectedu.data.model.User

data class ProfileState(
    val user: User = User(),
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,

    // Campos de edición
    val editName: String = "",
    val editUniversity: String = "",
    val editCareer: String = "",
    val editAge: String = "",

    // Errores
    val nameError: String? = null,
    val errorMessage: String? = null
)