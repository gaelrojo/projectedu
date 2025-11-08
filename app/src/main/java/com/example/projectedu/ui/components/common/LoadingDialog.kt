package com.example.projectedu.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.projectedu.ui.theme.BackgroundCard
import com.example.projectedu.ui.theme.PrimaryPurple
import com.example.projectedu.ui.theme.TextPrimary

@Composable
fun LoadingDialog(
    message: String = "Cargando..."
) {
    Dialog(onDismissRequest = { }) {
        Box(
            modifier = Modifier
                .background(BackgroundCard, RoundedCornerShape(16.dp))
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator(
                    color = PrimaryPurple,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = message,
                    color = TextPrimary,
                    fontSize = 16.sp
                )
            }
        }
    }
}