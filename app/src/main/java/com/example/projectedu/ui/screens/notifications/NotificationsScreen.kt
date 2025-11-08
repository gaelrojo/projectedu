package com.example.projectedu.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectedu.data.model.Notification
import com.example.projectedu.data.model.NotificationType
import com.example.projectedu.ui.components.common.BackButton
import com.example.projectedu.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    navController: NavController,
    viewModel: NotificationsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Notificaciones")
                        if (state.unreadCount > 0) {
                            Surface(
                                shape = CircleShape,
                                color = AccentRed
                            ) {
                                Text(
                                    text = state.unreadCount.toString(),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    BackButton(onClick = { navController.popBackStack() })
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark,
                    titleContentColor = TextPrimary
                ),
                actions = {
                    if (state.unreadCount > 0) {
                        TextButton(onClick = { viewModel.markAllAsRead() }) {
                            Text("Marcar todas", color = PrimaryPurple)
                        }
                    }
                }
            )
        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        if (state.notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "🔔",
                        fontSize = 64.sp
                    )
                    Text(
                        text = "No tienes notificaciones",
                        fontSize = 18.sp,
                        color = TextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Sección: Última semana
                item {
                    Text(
                        text = "Última semana",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                val recentNotifications = state.notifications.filter {
                    it.timestamp.time > System.currentTimeMillis() - 604800000 // 7 días
                }

                items(recentNotifications) { notification ->
                    NotificationCard(
                        notification = notification,
                        onClick = { viewModel.markAsRead(notification.id) }
                    )
                }

                // Sección: Últimas recta (más antiguas)
                val olderNotifications = state.notifications.filter {
                    it.timestamp.time <= System.currentTimeMillis() - 604800000
                }

                if (olderNotifications.isNotEmpty()) {
                    item {
                        Text(
                            text = "Última recta",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                        )
                    }

                    items(olderNotifications) { notification ->
                        NotificationCard(
                            notification = notification,
                            onClick = { viewModel.markAsRead(notification.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: Notification,
    onClick: () -> Unit
) {
    val iconData = when (notification.type) {
        NotificationType.TASK_REMINDER -> Pair(Icons.Default.Alarm, AccentYellow)
        NotificationType.TASK_COMPLETED -> Pair(Icons.Default.CheckCircle, AccentGreen)
        NotificationType.LEVEL_UP -> Pair(Icons.Default.TrendingUp, LevelBadge)
        NotificationType.BADGE_UNLOCKED -> Pair(Icons.Default.EmojiEvents, AchievementGold)
        NotificationType.STREAK_MILESTONE -> Pair(Icons.Default.LocalFireDepartment, StreakFire)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) BackgroundCard.copy(alpha = 0.6f) else BackgroundCard
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconData.second.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconData.first,
                    contentDescription = null,
                    tint = iconData.second,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 16.sp,
                        fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.SemiBold,
                        color = if (notification.isRead) TextSecondary else TextPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(PrimaryPurple)
                        )
                    }
                }

                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = TextSecondary
                )

                Text(
                    text = getRelativeTime(notification.timestamp),
                    fontSize = 12.sp,
                    color = TextTertiary
                )
            }
        }
    }
}

fun getRelativeTime(date: Date): String {
    val diff = System.currentTimeMillis() - date.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 0 -> "Hace ${days}d"
        hours > 0 -> "Hace ${hours}h"
        minutes > 0 -> "Hace ${minutes}m"
        else -> "Ahora"
    }
}