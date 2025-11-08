package com.example.projectedu.ui.components.navigation

import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projectedu.ui.navigation.Screen
import com.example.projectedu.ui.theme.*

@Composable
fun DrawerContent(
    navController: NavController,
    currentRoute: String?,
    userName: String,
    userEmail: String,
    userLevel: Int,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = BackgroundCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header con perfil
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(PrimaryPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.split(" ")
                            .mapNotNull { it.firstOrNull()?.toString() }
                            .take(2)
                            .joinToString(""),
                        color = TextPrimary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = userName,
                    fontSize = 18.sp,
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
                    shape = CircleShape,
                    color = LevelBadge.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "Nivel $userLevel",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = LevelBadge
                    )
                }
            }

            Divider(color = SurfaceBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Menu Items
            DrawerMenuItem(
                icon = Icons.Default.Home,
                title = "Inicio",
                isSelected = currentRoute == Screen.Home.route,
                onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.Task,
                title = "Tareas",
                isSelected = currentRoute == Screen.Tasks.route,
                onClick = {
                    navController.navigate(Screen.Tasks.route)
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.CalendarMonth,
                title = "Calendario",
                isSelected = currentRoute == Screen.Calendar.route,
                onClick = {
                    navController.navigate(Screen.Calendar.route)
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.Notifications,
                title = "Notificaciones",
                isSelected = currentRoute == Screen.Notifications.route,
                onClick = {
                    navController.navigate(Screen.Notifications.route)
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.ShoppingCart,
                title = "Tienda",
                isSelected = currentRoute == Screen.Store.route,
                onClick = {
                    navController.navigate(Screen.Store.route)
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.Person,
                title = "Mi Perfil",
                isSelected = currentRoute == Screen.Profile.route,
                onClick = {
                    navController.navigate(Screen.Profile.route)
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Divider(color = SurfaceBorder, modifier = Modifier.padding(vertical = 8.dp))

            DrawerMenuItem(
                icon = Icons.Default.Settings,
                title = "Configuración",
                isSelected = currentRoute == Screen.Settings.route,
                onClick = {
                    navController.navigate(Screen.Settings.route)
                    onCloseDrawer()
                }
            )
        }
    }
}

@Composable
fun DrawerMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
            .background(if (isSelected) PrimaryPurple.copy(alpha = 0.2f) else androidx.compose.ui.graphics.Color.Transparent)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) PrimaryPurple else TextSecondary,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) PrimaryPurple else TextPrimary
        )
    }
}