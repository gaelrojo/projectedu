package com.example.projectedu.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectedu.ui.navigation.Screen
import com.example.projectedu.ui.theme.*

@Composable
fun DrawerContent(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onCloseDrawer: () -> Unit,
    userName: String = "Usuario",
    userEmail: String = "usuario@ejemplo.com",
    userLevel: Int = 1
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Header con información del usuario
        DrawerHeader(
            userName = userName,
            userEmail = userEmail,
            userLevel = userLevel
        )

        Divider(color = SurfaceVariant.copy(alpha = 0.3f))

        // Items del menú
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            DrawerMenuItem(
                icon = Icons.Default.Home,
                title = "Inicio",
                isSelected = currentRoute == Screen.Home.route,
                onClick = {
                    onNavigate(Screen.Home.route)
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.CheckCircle,
                title = "Tareas",
                isSelected = currentRoute == Screen.Tasks.route,
                onClick = {
                    onNavigate(Screen.Tasks.route)
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.DateRange,
                title = "Calendario",
                isSelected = currentRoute == Screen.Calendar.route,
                onClick = {
                    onNavigate(Screen.Calendar.route)
                    onCloseDrawer()
                }
            )

            // ← NUEVO ITEM: MATERIAS
            DrawerMenuItem(
                icon = Icons.Default.Book,
                title = "Materias",
                isSelected = currentRoute == Screen.Subjects.route,
                onClick = {
                    onNavigate(Screen.Subjects.route)
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.Notifications,
                title = "Notificaciones",
                isSelected = currentRoute == "notifications",
                onClick = {
                    // TODO: Implementar pantalla de notificaciones
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.ShoppingCart,
                title = "Tienda",
                isSelected = currentRoute == Screen.Store.route,
                onClick = {
                    onNavigate(Screen.Store.route)
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.Person,
                title = "Mi Perfil",
                isSelected = currentRoute == Screen.Profile.route,
                onClick = {
                    onNavigate(Screen.Profile.route)
                    onCloseDrawer()
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Divider(color = SurfaceVariant.copy(alpha = 0.3f))

        // Configuración al final
        DrawerMenuItem(
            icon = Icons.Default.Settings,
            title = "Configuración",
            isSelected = currentRoute == Screen.Settings.route,
            onClick = {
                onNavigate(Screen.Settings.route)
                onCloseDrawer()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DrawerHeader(
    userName: String,
    userEmail: String,
    userLevel: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryPurple.copy(alpha = 0.1f))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar con iniciales
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(PrimaryPurple),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userName.take(2).uppercase(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = userName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Text(
            text = userEmail,
            fontSize = 14.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Badge de nivel
        Surface(
            shape = MaterialTheme.shapes.small,
            color = AccentGold.copy(alpha = 0.2f)
        ) {
            Text(
                text = "Nivel $userLevel",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AccentGold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) PrimaryPurple.copy(alpha = 0.15f) else DarkBackground
    val contentColor = if (isSelected) PrimaryPurple else TextSecondary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) TextPrimary else TextSecondary
        )
    }
}