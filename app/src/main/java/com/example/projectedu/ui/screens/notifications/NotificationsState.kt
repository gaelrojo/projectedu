package com.example.projectedu.ui.screens.notifications

import com.example.projectedu.data.model.Notification

data class NotificationsState(
    val notifications: List<Notification> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = false
)