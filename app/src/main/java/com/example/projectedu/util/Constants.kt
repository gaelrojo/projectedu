package com.example.projectedu.util

object Constants {

    // GAMIFICATION
    const val XP_LOW_PRIORITY = 10
    const val XP_MEDIUM_PRIORITY = 25
    const val XP_HIGH_PRIORITY = 50

    const val XP_MULTIPLIER_EARLY = 1.5f
    const val XP_MULTIPLIER_ON_TIME = 1.0f
    const val XP_MULTIPLIER_LATE = 0.5f

    const val XP_BASE_PER_LEVEL = 100
    const val XP_LEVEL_INCREMENT = 50
    const val MAX_LEVEL = 50

    // PRIORITIES
    object Priority {
        const val LOW = "Baja"
        const val MEDIUM = "Media"
        const val HIGH = "Alta"
    }

    // TASK TYPES
    object TaskType {
        const val EXAM = "Examen"
        const val HOMEWORK = "Tarea"
        const val PROJECT = "Proyecto"
        const val READING = "Lectura"
        const val STUDY = "Estudio"
        const val OTHER = "Otro"
    }

    // VALIDATION
    const val MIN_PASSWORD_LENGTH = 6
    const val MAX_PASSWORD_LENGTH = 50
    const val MIN_NAME_LENGTH = 2
    const val MAX_NAME_LENGTH = 50
    const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"

    // ERROR MESSAGES
    object ErrorMessages {
        const val INVALID_EMAIL = "Correo electrónico inválido"
        const val INVALID_PASSWORD = "La contraseña debe tener al menos 6 caracteres"
        const val PASSWORDS_DONT_MATCH = "Las contraseñas no coinciden"
        const val EMPTY_FIELD = "Este campo no puede estar vacío"
        const val INVALID_NAME = "El nombre debe tener al menos 2 caracteres"
        const val LOGIN_FAILED = "Email o contraseña incorrectos"
        const val REGISTRATION_FAILED = "No se pudo completar el registro"
        const val GENERIC_ERROR = "Ha ocurrido un error. Intenta de nuevo"
    }

    // SUCCESS MESSAGES
    object SuccessMessages {
        const val LOGIN_SUCCESS = "¡Bienvenido de vuelta!"
        const val REGISTRATION_SUCCESS = "¡Registro exitoso!"
        const val TASK_CREATED = "Tarea creada exitosamente"
        const val TASK_COMPLETED = "¡Tarea completada! +%d XP"
        const val TASK_DELETED = "Tarea eliminada"
        const val LEVEL_UP = "¡Subiste al nivel %d!"
        const val BADGE_UNLOCKED = "¡Nueva insignia desbloqueada!"
        const val PASSWORD_RESET_SENT = "Se ha enviado un correo para restablecer tu contraseña"
    }

    // BADGES
    object BadgeIds {
        const val FIRST_TASK = "first_task"
        const val PERFECT_WEEK = "perfect_week"
        const val EARLY_BIRD = "early_bird"
        const val SPEEDRUN = "speedrun"
        const val CENTURY = "century"
        const val HALF_CENTURY = "half_century"
        const val DEDICATION = "dedication"
    }
}