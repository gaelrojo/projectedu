package com.example.projectedu.data.model



import androidx.compose.ui.graphics.Color

/**
 * Paleta de colores predefinidos para las materias
 * Cada color tiene un nombre descriptivo y un valor hexadecimal
 */
object SubjectColor {

    // Colores disponibles para materias
    val Red = Color(0xFFFF6B6B)
    val Orange = Color(0xFFFFB366)
    val Yellow = Color(0xFFFFE66D)
    val Green = Color(0xFF4ECDC4)
    val Blue = Color(0xFF6C5CE7)
    val Purple = Color(0xFFA29BFE)
    val Pink = Color(0xFFFF85A2)
    val Teal = Color(0xFF00CEC9)
    val Indigo = Color(0xFF5F27CD)
    val Lime = Color(0xFFB8E986)
    val Cyan = Color(0xFF00D2D3)
    val Magenta = Color(0xFFE056FD)

    /**
     * Lista de todos los colores disponibles
     * Útil para mostrar un selector de colores
     */
    val availableColors = listOf(
        ColorOption("Rojo", Red, "#FF6B6B"),
        ColorOption("Naranja", Orange, "#FFB366"),
        ColorOption("Amarillo", Yellow, "#FFE66D"),
        ColorOption("Verde", Green, "#4ECDC4"),
        ColorOption("Azul", Blue, "#6C5CE7"),
        ColorOption("Púrpura", Purple, "#A29BFE"),
        ColorOption("Rosa", Pink, "#FF85A2"),
        ColorOption("Turquesa", Teal, "#00CEC9"),
        ColorOption("Índigo", Indigo, "#5F27CD"),
        ColorOption("Lima", Lime, "#B8E986"),
        ColorOption("Cian", Cyan, "#00D2D3"),
        ColorOption("Magenta", Magenta, "#E056FD")
    )

    /**
     * Obtiene el color Color a partir de un string hexadecimal
     * @param hexColor String en formato "#RRGGBB"
     * @return Color correspondiente, o Red si el formato es inválido
     */
    fun fromHex(hexColor: String): Color {
        return try {
            val cleanHex = hexColor.removePrefix("#")
            val colorInt = cleanHex.toLong(16)
            Color(0xFF000000 or colorInt)
        } catch (e: Exception) {
            Red // Color por defecto si hay error
        }
    }

    /**
     * Convierte un Color a string hexadecimal
     * @param color El color a convertir
     * @return String en formato "#RRGGBB"
     */
    fun toHex(color: Color): String {
        val red = (color.red * 255).toInt()
        val green = (color.green * 255).toInt()
        val blue = (color.blue * 255).toInt()
        return String.format("#%02X%02X%02X", red, green, blue)
    }

    /**
     * Obtiene el nombre descriptivo de un color a partir de su valor hexadecimal
     * @param hexColor String en formato "#RRGGBB"
     * @return Nombre del color o el hex si no se encuentra
     */
    fun getColorName(hexColor: String): String {
        return availableColors.find { it.hex.equals(hexColor, ignoreCase = true) }?.name
            ?: hexColor
    }
}

/**
 * Data class que representa una opción de color
 * @param name Nombre descriptivo del color
 * @param color El color en formato Compose Color
 * @param hex Valor hexadecimal del color
 */
data class ColorOption(
    val name: String,
    val color: Color,
    val hex: String
)