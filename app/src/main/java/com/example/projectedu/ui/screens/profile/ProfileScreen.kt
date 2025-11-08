package com.example.projectedu.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectedu.ui.components.common.*
import com.example.projectedu.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                actions = {
                    if (!state.isEditing) {
                        IconButton(onClick = { viewModel.onEditClick() }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = TextPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Avatar y nombre
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(PrimaryPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.user.name.split(" ")
                            .mapNotNull { it.firstOrNull()?.toString() }
                            .take(2)
                            .joinToString(""),
                        color = TextPrimary,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (!state.isEditing) {
                    Text(
                        text = state.user.name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = state.user.email,
                        fontSize = 16.sp,
                        color = TextSecondary
                    )
                }

                // Card de gamificación
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = BackgroundCard
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Estadísticas",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        // Nivel
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = LevelBadge,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Nivel",
                                    fontSize = 16.sp,
                                    color = TextSecondary
                                )
                            }
                            Text(
                                text = state.user.currentLevel.toString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = LevelBadge
                            )
                        }

                        // XP actual
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = PrimaryPurple,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Experiencia",
                                    fontSize = 16.sp,
                                    color = TextSecondary
                                )
                            }
                            Text(
                                text = "${state.user.currentXP} XP",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryPurple
                            )
                        }

                        // Progreso
                        LinearProgressIndicator(
                            progress = { viewModel.getXPProgress() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = PrimaryPurple,
                            trackColor = XPBarBackground
                        )

                        Text(
                            text = "${viewModel.calculateXPForNextLevel() - state.user.currentXP} XP para nivel ${state.user.currentLevel + 1}",
                            fontSize = 14.sp,
                            color = TextTertiary
                        )

                        Divider(color = SurfaceBorder)

                        // Racha
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "🔥",
                                    fontSize = 24.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Racha actual",
                                    fontSize = 16.sp,
                                    color = TextSecondary
                                )
                            }
                            Text(
                                text = "${state.user.currentStreak} días",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = StreakFire
                            )
                        }

                        // Total XP
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = AccentGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "XP Total",
                                    fontSize = 16.sp,
                                    color = TextSecondary
                                )
                            }
                            Text(
                                text = "${state.user.totalXP} XP",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AccentGreen
                            )
                        }
                    }
                }

                // Información personal
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = BackgroundCard
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Información Personal",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        if (state.isEditing) {
                            // Modo edición
                            CustomTextField(
                                value = state.editName,
                                onValueChange = { viewModel.onNameChange(it) },
                                label = "Nombre completo",
                                leadingIcon = Icons.Default.Person,
                                isError = state.nameError != null,
                                errorMessage = state.nameError
                            )

                            CustomTextField(
                                value = state.editUniversity,
                                onValueChange = { viewModel.onUniversityChange(it) },
                                label = "Universidad",
                                leadingIcon = Icons.Default.School
                            )

                            CustomTextField(
                                value = state.editCareer,
                                onValueChange = { viewModel.onCareerChange(it) },
                                label = "Carrera",
                                leadingIcon = Icons.Default.Book
                            )

                            CustomTextField(
                                value = state.editAge,
                                onValueChange = { viewModel.onAgeChange(it) },
                                label = "Edad",
                                leadingIcon = Icons.Default.Cake,
                                keyboardType = KeyboardType.Number
                            )

                            // Mostrar error general si existe
                            state.errorMessage?.let { error ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = AccentRed.copy(alpha = 0.1f)
                                    )
                                ) {
                                    Text(
                                        text = error,
                                        color = AccentRed,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                SecondaryButton(
                                    text = "Cancelar",
                                    onClick = { viewModel.onCancelEdit() },
                                    modifier = Modifier.weight(1f)
                                )

                                PrimaryButton(
                                    text = "Guardar",
                                    onClick = { viewModel.onSaveClick() },
                                    modifier = Modifier.weight(1f),
                                    isLoading = state.isSaving
                                )
                            }

                        } else {
                            // Modo visualización
                            InfoRow(
                                icon = Icons.Default.School,
                                label = "Universidad",
                                value = state.user.university
                            )

                            InfoRow(
                                icon = Icons.Default.Book,
                                label = "Carrera",
                                value = state.user.career
                            )

                            InfoRow(
                                icon = Icons.Default.Cake,
                                label = "Edad",
                                value = "${state.user.age} años"
                            )
                        }
                    }
                }

                // Insignias
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = BackgroundCard
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Insignias",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = PrimaryPurple.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "${state.user.badges.size}/10",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PrimaryPurple
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            BadgeItem(emoji = "🎯", title = "Primera\nTarea", unlocked = true)
                            BadgeItem(emoji = "⭐", title = "Semana\nPerfecta", unlocked = true)
                            BadgeItem(emoji = "🌅", title = "Madrugador", unlocked = true)
                            BadgeItem(emoji = "⚡", title = "Veloz", unlocked = false)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }

            // Snackbar de éxito
            if (state.saveSuccess) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    containerColor = AccentGreen
                ) {
                    Text("✓ Perfil actualizado exitosamente")
                }
            }

            if (state.isSaving) {
                LoadingDialog(message = "Guardando cambios...")
            }
        }
    }
}

@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryPurple,
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = label,
                fontSize = 14.sp,
                color = TextSecondary
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun BadgeItem(
    emoji: String,
    title: String,
    unlocked: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    if (unlocked) AchievementGold.copy(alpha = 0.2f)
                    else TextDisabled.copy(alpha = 0.1f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 32.sp,
                modifier = if (!unlocked) {
                    Modifier.alpha(0.3f)
                } else {
                    Modifier
                }
            )
        }
        Text(
            text = title,
            fontSize = 12.sp,
            color = if (unlocked) TextSecondary else TextDisabled,
            fontWeight = if (unlocked) FontWeight.Medium else FontWeight.Normal
        )
    }
}