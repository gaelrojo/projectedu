package com.example.projectedu.ui.screens.profile

import com.example.projectedu.data.model.User

data class ProfileState(
    val user: User = User(),
    val isEditing: Boolean = false,
    val editName: String = "",
    val editUniversity: String = "",
    val editCareer: String = "",
    val editAge: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)