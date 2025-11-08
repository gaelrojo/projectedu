package com.example.projectedu.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectedu.ui.components.common.PrimaryButton
import com.example.projectedu.ui.components.common.SecondaryButton
import com.example.projectedu.ui.theme.*

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // Animación de entrada
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo/Título animado
            Text(
                text = "📚",
                fontSize = 80.sp,
                modifier = Modifier.scale(scale)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "ProjectEdu",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tu compañero de estudio gamificado",
                fontSize = 16.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Características
            FeatureItem(
                emoji = "🎯",
                title = "Organiza tus tareas",
                description = "Gestiona todas tus tareas académicas en un solo lugar"
            )

            Spacer(modifier = Modifier.height(16.dp))

            FeatureItem(
                emoji = "🏆",
                title = "Gana experiencia",
                description = "Sube de nivel completando tareas y desbloquea logros"
            )

            Spacer(modifier = Modifier.height(16.dp))

            FeatureItem(
                emoji = "📊",
                title = "Mide tu progreso",
                description = "Visualiza tu avance y mantén rachas diarias"
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botones
            PrimaryButton(
                text = "Registrarse",
                onClick = onNavigateToRegister
            )

            Spacer(modifier = Modifier.height(16.dp))

            SecondaryButton(
                text = "Iniciar sesión",
                onClick = onNavigateToLogin
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Universidad Politécnica de Pachuca",
                fontSize = 12.sp,
                color = TextTertiary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun FeatureItem(
    emoji: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            fontSize = 32.sp
        )

        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }
}
