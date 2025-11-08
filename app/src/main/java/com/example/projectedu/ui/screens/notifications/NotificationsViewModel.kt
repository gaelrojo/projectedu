package com.example.projectedu.ui.screens.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectedu.data.model.Notification
import com.example.projectedu.data.model.NotificationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class NotificationsViewModel : ViewModel() {

    private val _state = MutableStateFlow(NotificationsState())
    val state: StateFlow<NotificationsState> = _state.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        val mockNotifications = listOf(
            Notification(
                id = "1",
                title = "¡Actividad que vence en 2h hrs!",
                message = "Examen de Móviles 2 - No olvides estudiar",
                type = NotificationType.TASK_REMINDER,
                isRead = false,
                timestamp = Date(System.currentTimeMillis() - 3600000)
            ),
            Notification(
                id = "2",
                title = "¡Marca como entregada!",
                message = "Práctica de Base de Datos - Completada",
                type = NotificationType.TASK_COMPLETED,
                isRead = false,
                timestamp = Date(System.currentTimeMillis() - 7200000)
            ),
            Notification(
                id = "3",
                title = "Últimas semanas",
                message = "Matemáticas - Clase sobre integrales",
                type = NotificationType.TASK_REMINDER,
                isRead = true,
                timestamp = Date(System.currentTimeMillis() - 86400000)
            ),
            Notification(
                id = "4",
                title = "Últimas recta",
                message = "Ciencias - Tarea de laboratorio pendiente",
                type = NotificationType.TASK_REMINDER,
                isRead = true,
                timestamp = Date(System.currentTimeMillis() - 172800000)
            )
        )

        val unreadCount = mockNotifications.count { !it.isRead }

        _state.value = _state.value.copy(
            notifications = mockNotifications,
            unreadCount = unreadCount
        )
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            val updatedNotifications = _state.value.notifications.map { notification ->
                if (notification.id == notificationId) {
                    notification.copy(isRead = true)
                } else notification
            }

            val unreadCount = updatedNotifications.count { !it.isRead }

            _state.value = _state.value.copy(
                notifications = updatedNotifications,
                unreadCount = unreadCount
            )
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            val updatedNotifications = _state.value.notifications.map { it.copy(isRead = true) }

            _state.value = _state.value.copy(
                notifications = updatedNotifications,
                unreadCount = 0
            )
        }
    }
}