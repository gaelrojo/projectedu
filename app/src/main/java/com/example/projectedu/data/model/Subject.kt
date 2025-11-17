package com.example.projectedu.data.model

import androidx.compose.ui.graphics.Color

data class Subject(
    val id: String = "",
    val name: String = "",
    val professor: String = "",
    val schedule: String = "",
    val classroom: String = "",
    val color: Long = 0xFF8B7FFF, // Guardamos como Long para serialización
    val userId: String = ""
) {
    // Propiedad computada para obtener el Color
    val colorValue: Color
        get() = Color(color)
}

// Paleta de colores predefinidos para materias
object SubjectColors {
    val colors = listOf(
        0xFF8B7FFF, // Morado
        0xFF4A90E2, // Azul
        0xFF4CAF50, // Verde
        0xFFFFD700, // Dorado
        0xFFFF6B6B, // Rojo
        0xFFFF9F43, // Naranja
        0xFF5F27CD, // Morado oscuro
        0xFF00D2D3, // Cyan
        0xFFEE5A6F, // Rosa
        0xFF4834DF, // Azul índigo
        0xFF26DE81, // Verde claro
        0xFFFC5C65  // Rojo coral
    )

    fun getColorName(colorLong: Long): String {
        return when (colorLong) {
            0xFF8B7FFF -> "Morado"
            0xFF4A90E2 -> "Azul"
            0xFF4CAF50 -> "Verde"
            0xFFFFD700 -> "Dorado"
            0xFFFF6B6B -> "Rojo"
            0xFFFF9F43 -> "Naranja"
            0xFF5F27CD -> "Morado Oscuro"
            0xFF00D2D3 -> "Cyan"
            0xFFEE5A6F -> "Rosa"
            0xFF4834DF -> "Índigo"
            0xFF26DE81 -> "Verde Claro"
            0xFFFC5C65 -> "Coral"
            else -> "Morado"
        }
    }
}