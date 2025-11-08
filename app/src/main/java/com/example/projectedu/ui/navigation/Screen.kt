package com.example.projectedu.ui.navigation

sealed class Screen(val route: String) {
    // Auth Flow
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Recovery : Screen("recovery")

    // Main App Flow
    object Home : Screen("home")
    object Tasks : Screen("tasks")
    object Notifications : Screen("notifications")
    object Profile : Screen("profile")

    // Details
    object TaskDetail : Screen("task_detail/{taskId}") {
        fun createRoute(taskId: String) = "task_detail/$taskId"
    }
    object SubjectDetail : Screen("subject_detail/{subjectId}") {
        fun createRoute(subjectId: String) = "subject_detail/$subjectId"
    }

    object Calendar : Screen("calendar")
}