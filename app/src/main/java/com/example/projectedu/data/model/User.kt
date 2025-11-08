package com.example.projectedu.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val university: String = "Universidad Politécnica de Pachuca",
    val career: String = "Ingeniería en Software",
    val age: Int = 21,
    val currentLevel: Int = 1,
    val currentXP: Int = 0,
    val totalXP: Int = 0,
    val currentStreak: Int = 0,
    val badges: List<String> = emptyList(),
    val profileImageUrl: String = ""
)