package com.example.projectedu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.projectedu.ui.navigation.NavGraph
import com.example.projectedu.ui.theme.ProjecteduTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjecteduTheme {
                val navController = rememberNavController()

                // ✅ CORRECCIÓN: Agregar onMenuClick
                NavGraph(
                    navController = navController,
                      // Por ahora vacío, o puedes agregar lógica
                )
            }
        }
    }
}