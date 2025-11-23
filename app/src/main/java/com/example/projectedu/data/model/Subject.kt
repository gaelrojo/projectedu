package com.example.projectedu.data.model

data class Subject(
    val id: String,
    val userId: String,
    val name: String,
    val color: String,
    val professorName: String = "",
    val classroom: String = "",
    val schedule: String = ""
)