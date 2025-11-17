package com.example.projectedu.ui.screens.home

import com.example.projectedu.data.model.User

data class HomeState(
    val user: User = User(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)