package com.example.projectedu.data.repository

import com.example.projectedu.data.model.User

class AuthRepository {

    // Simulación de usuario actual (en producción esto vendría de Firebase Auth)
    private var currentUser: User? = User(
        id = "user123",
        name = "Usuario Demo",
        email = "demo@example.com"
    )

    fun getCurrentUser(): User? {
        return currentUser
    }

    fun setCurrentUser(user: User?) {
        currentUser = user
    }

    fun isUserLoggedIn(): Boolean {
        return currentUser != null
    }
}