package com.example.projectedu.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.projectedu.ui.screens.WelcomeScreen
import com.example.projectedu.ui.screens.auth.LoginScreen
import com.example.projectedu.ui.screens.auth.RegisterScreen
import com.example.projectedu.ui.screens.auth.RecoveryScreen
import com.example.projectedu.ui.screens.home.HomeScreen
import com.example.projectedu.ui.screens.profile.ProfileScreen
import com.example.projectedu.ui.screens.tasks.TasksScreen
import com.example.projectedu.ui.screens.notifications.NotificationsScreen
import com.example.projectedu.ui.screens.calendar.CalendarScreen
import com.example.projectedu.ui.screens.subjects.SubjectsScreen // ← NUEVO IMPORT

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Welcome.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Welcome Screen
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // Login Screen
        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRecovery = {
                    navController.navigate(Screen.Recovery.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Register Screen
        composable(route = Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Recovery Screen
        composable(route = Screen.Recovery.route) {
            RecoveryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Recovery.route) { inclusive = true }
                    }
                }
            )
        }

        // Home Screen (con Navigation Drawer)
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // Tasks Screen
        composable(route = Screen.Tasks.route) {
            TasksScreen(navController = navController)
        }

        // Calendar Screen
        composable(route = Screen.Calendar.route) {
            CalendarScreen(navController = navController)
        }

        // Subjects Screen - ← NUEVO
        composable(route = Screen.Subjects.route) {
            SubjectsScreen(navController = navController)
        }

        // Notifications Screen
        composable(route = Screen.Notifications.route) {
            NotificationsScreen(navController = navController)
        }

        // Profile Screen
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}