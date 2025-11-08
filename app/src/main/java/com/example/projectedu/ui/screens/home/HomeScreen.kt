package com.example.projectedu.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
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
import com.example.projectedu.ui.components.navigation.BottomNavigationBar
import com.example.projectedu.ui.navigation.Screen
import com.example.projectedu.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundDark)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header con saludo y notificaciones
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Hola",
                                fontSize = 16.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = state.user.name.split(" ").firstOrNull() ?: "Usuario",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                navController.navigate(Screen.Notifications.route)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notificaciones",
                                    tint = TextPrimary
                                )
                            }

                            // Avatar (clickeable para ir a perfil)
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryPurple)
                                    .clickable {
                                        navController.navigate(Screen.Profile.route)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = state.user.name.split(" ")
                                        .mapNotNull { it.firstOrNull()?.toString() }
                                        .take(2)
                                        .joinToString(""),
                                    color = TextPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Card de nivel y XP
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = BackgroundCard
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Este es tu Studyboard",
                                    fontSize = 16.sp,
                                    color = TextSecondary
                                )

                                // Badge de nivel
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = LevelBadge
                                ) {
                                    Text(
                                        text = "General",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = BackgroundDark
                                    )
                                }
                            }

                            // Nivel
                            Row(
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = "Nivel ",
                                    fontSize = 20.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = state.user.currentLevel.toString(),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LevelBadge
                                )
                            }

                            // Barra de progreso XP
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${state.user.currentXP} XP",
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "${viewModel.calculateXPForNextLevel()} XP",
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )
                                }

                                LinearProgressIndicator(
                                    progress = { viewModel.getXPProgress() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = PrimaryPurple,
                                    trackColor = XPBarBackground
                                )
                            }
                        }
                    }
                }

                // Progreso de la semana
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = BackgroundCard
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Progreso en Tareas",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Esta Semana",
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )
                                }

                                Text(
                                    text = "${(state.weekProgress * 100).toInt()}%",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryPurple
                                )
                            }

                            LinearProgressIndicator(
                                progress = { state.weekProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(12.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                color = AccentGreen,
                                trackColor = XPBarBackground
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                StatItem(
                                    label = "Tareas completadas",
                                    value = state.completedTasksThisWeek.toString()
                                )
                                StatItem(
                                    label = "Total de tareas",
                                    value = state.totalTasksThisWeek.toString()
                                )
                            }
                        }
                    }
                }

                // Tareas próximas
                item {
                    Text(
                        text = "Tareas Próximas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                items(state.upcomingTasks) { task ->
                    TaskCard(task = task)
                }

                // Materias
                item {
                    Text(
                        text = "Tus Materias",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                items(state.recentSubjects) { subject ->
                    SubjectCard(subject = subject)
                }

                // Spacer final
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}

@Composable
fun TaskCard(task: com.example.projectedu.data.model.Task) {
    val priorityColor = when (task.priority) {
        "Alta" -> PriorityHigh
        "Media" -> PriorityMedium
        else -> PriorityLow
    }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundCard
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Indicador de prioridad
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(48.dp)
                        .background(priorityColor, RoundedCornerShape(2.dp))
                )

                Column {
                    Text(
                        text = task.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = task.subjectName,
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = dateFormat.format(task.dueDate),
                        fontSize = 12.sp,
                        color = TextTertiary
                    )
                }
            }

            // XP Badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = PrimaryPurple.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "+${task.xpReward} XP",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurple
                )
            }
        }
    }
}

@Composable
fun SubjectCard(subject: com.example.projectedu.data.model.Subject) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundCard
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Color indicator
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(androidx.compose.ui.graphics.Color(subject.color))
                )

                Column {
                    Text(
                        text = subject.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = subject.teacher,
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "${subject.completedTasks}/${subject.totalTasks} tareas",
                        fontSize = 12.sp,
                        color = TextTertiary
                    )
                }
            }

            // Progress percentage
            Text(
                text = "${subject.progressPercentage.toInt()}%",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AccentGreen
            )
        }
    }
}