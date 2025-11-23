package com.example.projectedu.ui.screens.subjects

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.projectedu.data.model.Subject
import com.example.projectedu.data.model.SubjectColor
import java.util.UUID

/**
 * Diálogo para agregar o editar una materia
 * @param subject Materia a editar (null si es nueva)
 * @param onDismiss Callback cuando se cierra el diálogo
 * @param onSave Callback cuando se guarda la materia
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSubjectDialog(
    subject: Subject? = null,
    onDismiss: () -> Unit,
    onSave: (Subject) -> Unit
) {
    // Estados del formulario
    var name by remember { mutableStateOf(subject?.name ?: "") }
    var professor by remember { mutableStateOf(subject?.professorName ?: "") }
    var schedule by remember { mutableStateOf(subject?.schedule ?: "") }
    var classroom by remember { mutableStateOf(subject?.classroom ?: "") }
    var selectedColor by remember { mutableStateOf(subject?.color ?: "#FF6B6B") }

    // Estados de validación
    var nameError by remember { mutableStateOf(false) }

    // Estado para mostrar el selector de colores
    var showColorPicker by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                // Título
                Text(
                    text = if (subject == null) "Nueva Materia" else "Editar Materia",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Campo: Nombre de la materia
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    label = { Text("Nombre de la materia *") },
                    isError = nameError,
                    supportingText = {
                        if (nameError) {
                            Text("El nombre es obligatorio")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
                )

                // Campo: Profesor
                OutlinedTextField(
                    value = professor,
                    onValueChange = { professor = it },
                    label = { Text("Profesor(a)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
                )

                // Campo: Horario
                OutlinedTextField(
                    value = schedule,
                    onValueChange = { schedule = it },
                    label = { Text("Horario") },
                    placeholder = { Text("Ej: Lun-Mier 8:00-10:00") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
                )

                // Campo: Aula
                OutlinedTextField(
                    value = classroom,
                    onValueChange = { classroom = it },
                    label = { Text("Aula") },
                    placeholder = { Text("Ej: Aula 101") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    singleLine = true
                )

                // Selector de color
                Text(
                    text = "Color de la materia *",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Grid de colores
                LazyVerticalGrid(
                    columns = GridCells.Fixed(count = 6),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .height(120.dp)
                        .padding(bottom = 16.dp)
                ) {
                    items(count = SubjectColor.availableColors.size) { index ->
                        val colorOption = SubjectColor.availableColors[index]
                        ColorOption(
                            color = colorOption.color,
                            isSelected = selectedColor == colorOption.hex,
                            onClick = { selectedColor = colorOption.hex }
                        )
                    }
                }

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                nameError = true
                            } else {
                                val newSubject = Subject(
                                    id = subject?.id ?: UUID.randomUUID().toString(),
                                    name = name.trim(),
                                    professorName = professor.trim(),
                                    schedule = schedule.trim(),
                                    classroom = classroom.trim(),
                                    color = selectedColor,
                                    userId = subject?.userId ?: ""
                                )
                                onSave(newSubject)
                            }
                        }
                    ) {
                        Text(if (subject == null) "Agregar" else "Guardar")
                    }
                }
            }
        }
    }
}

/**
 * Componente individual de opción de color
 * @param color Color a mostrar
 * @param isSelected Si está seleccionado
 * @param onClick Callback al hacer clic
 */
@Composable
private fun ColorOption(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f),
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Seleccionado",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}