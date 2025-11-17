package com.example.projectedu.ui.screens.subjects

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.projectedu.data.model.Subject
import com.example.projectedu.data.model.SubjectColors
import com.example.projectedu.ui.theme.*

@Composable
fun AddEditSubjectDialog(
    subject: Subject?,
    onDismiss: () -> Unit,
    onSave: (Subject) -> Unit
) {
    var name by remember { mutableStateOf(subject?.name ?: "") }
    var professor by remember { mutableStateOf(subject?.professor ?: "") }
    var schedule by remember { mutableStateOf(subject?.schedule ?: "") }
    var classroom by remember { mutableStateOf(subject?.classroom ?: "") }
    var selectedColor by remember { mutableStateOf(subject?.color ?: 0xFF8B7FFF) }

    var nameError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text(
                    text = if (subject == null) "Nueva Materia" else "Editar Materia",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        if (it.length <= 50) {
                            name = it
                            nameError = false
                        }
                    },
                    label = { Text("Nombre de la materia *") },
                    isError = nameError,
                    supportingText = {
                        if (nameError) {
                            Text("El nombre es obligatorio")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8B7FFF),
                        focusedLabelColor = Color(0xFF8B7FFF),
                        cursorColor = Color(0xFF8B7FFF),
                        unfocusedBorderColor = TextSecondary.copy(alpha = 0.5f),
                        unfocusedLabelColor = TextSecondary,
                        errorBorderColor = Color(0xFFFF5252),
                        errorLabelColor = Color(0xFFFF5252)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = professor,
                    onValueChange = { if (it.length <= 50) professor = it },
                    label = { Text("Profesor") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8B7FFF),
                        focusedLabelColor = Color(0xFF8B7FFF),
                        cursorColor = Color(0xFF8B7FFF),
                        unfocusedBorderColor = TextSecondary.copy(alpha = 0.5f),
                        unfocusedLabelColor = TextSecondary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = schedule,
                    onValueChange = { if (it.length <= 50) schedule = it },
                    label = { Text("Horario") },
                    placeholder = { Text("Ej: Lun, Mié 08:00-10:00") },
                    leadingIcon = {
                        Icon(Icons.Default.Schedule, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8B7FFF),
                        focusedLabelColor = Color(0xFF8B7FFF),
                        cursorColor = Color(0xFF8B7FFF),
                        unfocusedBorderColor = TextSecondary.copy(alpha = 0.5f),
                        unfocusedLabelColor = TextSecondary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = classroom,
                    onValueChange = { if (it.length <= 30) classroom = it },
                    label = { Text("Aula/Salón") },
                    placeholder = { Text("Ej: Lab 3, Aula 201") },
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8B7FFF),
                        focusedLabelColor = Color(0xFF8B7FFF),
                        cursorColor = Color(0xFF8B7FFF),
                        unfocusedBorderColor = TextSecondary.copy(alpha = 0.5f),
                        unfocusedLabelColor = TextSecondary
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Color de la materia",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(120.dp)
                ) {
                    items(SubjectColors.colors) { color ->
                        ColorOption(
                            color = Color(color),
                            isSelected = selectedColor == color,
                            onClick = { selectedColor = color }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextPrimary
                        )
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                nameError = true
                            } else {
                                val newSubject = Subject(
                                    id = subject?.id ?: "",
                                    name = name.trim(),
                                    professor = professor.trim(),
                                    schedule = schedule.trim(),
                                    classroom = classroom.trim(),
                                    color = selectedColor,
                                    userId = subject?.userId ?: ""
                                )
                                onSave(newSubject)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8B7FFF)
                        )
                    ) {
                        Text(if (subject == null) "Guardar" else "Actualizar")
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorOption(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) TextPrimary else Color.Transparent,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Seleccionado",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}