package com.example.projectedu.data.model

import java.util.Date

data class Notification(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val type: NotificationType = NotificationType.TASK_REMINDER,
    val isRead: Boolean = false,
    val timestamp: Date = Date(),
    val taskId: String? = null
)

enum class NotificationType {
    TASK_REMINDER,
    TASK_COMPLETED,
    LEVEL_UP,
    BADGE_UNLOCKED,
    STREAK_MILESTONE
}
