package com.example.projectedu.ui.screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectedu.data.model.Task
import com.example.projectedu.ui.components.common.*
import com.example.projectedu.ui.components.navigation.BottomNavigationBar
import com.example.projectedu.ui.theme.*
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    navController: NavController,
    viewModel: TasksViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark,
                    titleContentColor = TextPrimary
                ),
                actions = {
                    IconButton(onClick = { viewModel.onShowCreateDialog() }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar tarea",
                            tint = PrimaryPurple
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filtros
            ScrollableTabRow(
                selectedTabIndex = state.selectedFilter.ordinal,
                containerColor = BackgroundDark,
                contentColor = TextPrimary,
                edgePadding = 16.dp
            ) {
                TaskFilter.values().forEach { filter ->
                    Tab(
                        selected = state.selectedFilter == filter,
                        onClick = { viewModel.onFilterChange(filter) },
                        text = {
                            Text(
                                text = when (filter) {
                                    TaskFilter.ALL -> "Todas"
                                    TaskFilter.PENDING -> "Pendientes"
                                    TaskFilter.COMPLETED -> "Completadas"
                                    TaskFilter.HIGH_PRIORITY -> "Urgentes"
                                },
                                fontWeight = if (state.selectedFilter == filter) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Divider(color = SurfaceBorder)

            // Lista de tareas
            if (state.filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "📝",
                            fontSize = 64.sp
                        )
                        Text(
                            text = "No hay tareas",
                            fontSize = 18.sp,
                            color = TextSecondary
                        )
                        PrimaryButton(
                            text = "Crear tarea",
                            onClick = { viewModel.onShowCreateDialog() },
                            modifier = Modifier.width(200.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.filteredTasks) { task ->
                        TaskItemCard(
                            task = task,
                            onToggleComplete = { viewModel.onToggleTaskCompletion(task) },
                            onEdit = { viewModel.onShowEditDialog(task) },
                            onDelete = { viewModel.onShowDeleteDialog(task) }
                        )
                    }
                }
            }
        }

        // Dialogs
        if (state.showCreateDialog) {
            TaskDialog(
                title = "Crear Tarea",
                taskTitle = state.editTitle,
                description = state.editDescription,
                subject = state.editSubject,
                priority = state.editPriority,
                type = state.editType,
                onTitleChange = { viewModel.onTitleChange(it) },
                onDescriptionChange = { viewModel.onDescriptionChange(it) },
                onSubjectChange = { viewModel.onSubjectChange(it) },
                onPriorityChange = { viewModel.onPriorityChange(it) },
                onTypeChange = { viewModel.onTypeChange(it) },
                onDismiss = { viewModel.onDismissDialogs() },
                onConfirm = { viewModel.onCreateTask() },
                isLoading = state.isLoading
            )
        }

        if (state.showEditDialog) {
            TaskDialog(
                title = "Editar Tarea",
                taskTitle = state.editTitle,
                description = state.editDescription,
                subject = state.editSubject,
                priority = state.editPriority,
                type = state.editType,
                onTitleChange = { viewModel.onTitleChange(it) },
                onDescriptionChange = { viewModel.onDescriptionChange(it) },
                onSubjectChange = { viewModel.onSubjectChange(it) },
                onPriorityChange = { viewModel.onPriorityChange(it) },
                onTypeChange = { viewModel.onTypeChange(it) },
                onDismiss = { viewModel.onDismissDialogs() },
                onConfirm = { viewModel.onUpdateTask() },
                isLoading = state.isLoading
            )
        }

        if (state.showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.onDismissDialogs() },
                title = { Text("Eliminar tarea") },
                text = { Text("¿Estás seguro de que deseas eliminar esta tarea?") },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.onDeleteTask() },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = AccentRed
                        )
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.onDismissDialogs() }) {
                        Text("Cancelar")
                    }
                },
                containerColor = BackgroundCard,
                titleContentColor = TextPrimary,
                textContentColor = TextSecondary
            )
        }

        // Snackbar de éxito
        state.successMessage?.let { message ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Snackbar(
                    containerColor = AccentGreen
                ) {
                    Text(message)
                }
            }
        }
    }
}

@Composable
fun TaskItemCard(
    task: Task,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val priorityColor = when (task.priority) {
        "Alta" -> PriorityHigh
        "Media" -> PriorityMedium
        else -> PriorityLow
    }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) BackgroundCard.copy(alpha = 0.6f) else BackgroundCard
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Checkbox
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { onToggleComplete() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = AccentGreen,
                            uncheckedColor = TextSecondary
                        )
                    )

                    // Indicador de prioridad
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(48.dp)
                            .background(priorityColor, RoundedCornerShape(2.dp))
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (task.isCompleted) TextDisabled else TextPrimary,
                            style = if (task.isCompleted) {
                                androidx.compose.ui.text.TextStyle(
                                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                )
                            } else {
                                androidx.compose.ui.text.TextStyle()
                            }
                        )

                        if (task.description.isNotEmpty()) {
                            Text(
                                text = task.description,
                                fontSize = 14.sp,
                                color = TextSecondary,
                                maxLines = 2
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = priorityColor.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = task.priority,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    color = priorityColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Text(
                                text = task.subjectName,
                                fontSize = 12.sp,
                                color = TextTertiary
                            )

                            Text(
                                text = "•",
                                fontSize = 12.sp,
                                color = TextTertiary
                            )

                            Text(
                                text = dateFormat.format(task.dueDate),
                                fontSize = 12.sp,
                                color = TextTertiary
                            )
                        }
                    }
                }

                // XP Badge
                if (!task.isCompleted) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PrimaryPurple.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "+${task.xpReward} XP",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryPurple
                        )
                    }
                }
            }

            // Botones de acción
            if (!task.isCompleted) {
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = SurfaceBorder
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = PrimaryPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar", color = PrimaryPurple)
                    }

                    TextButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = AccentRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Eliminar", color = AccentRed)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDialog(
    title: String,
    taskTitle: String,
    description: String,
    subject: String,
    priority: String,
    type: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSubjectChange: (String) -> Unit,
    onPriorityChange: (String) -> Unit,
    onTypeChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isLoading: Boolean
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            colors = CardDefaults.cardColors(
                containerColor = BackgroundCard
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                CustomTextField(
                    value = taskTitle,
                    onValueChange = onTitleChange,
                    label = "Título de la tarea",
                    leadingIcon = Icons.Default.Title
                )

                CustomTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = "Descripción",
                    leadingIcon = Icons.Default.Description
                )

                CustomTextField(
                    value = subject,
                    onValueChange = onSubjectChange,
                    label = "Materia",
                    leadingIcon = Icons.Default.Book
                )

                // Priority selector
                Column {
                    Text(
                        text = "Prioridad",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Baja", "Media", "Alta").forEach { p ->
                            FilterChip(
                                selected = priority == p,
                                onClick = { onPriorityChange(p) },
                                label = { Text(p) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = when (p) {
                                        "Alta" -> PriorityHigh
                                        "Media" -> PriorityMedium
                                        else -> PriorityLow
                                    },
                                    selectedLabelColor = TextPrimary
                                )
                            )
                        }
                    }
                }

                // Type selector
                Column {
                    Text(
                        text = "Tipo",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Tarea", "Examen", "Proyecto").forEach { t ->
                            FilterChip(
                                selected = type == t,
                                onClick = { onTypeChange(t) },
                                label = { Text(t) }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SecondaryButton(
                        text = "Cancelar",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    )

                    PrimaryButton(
                        text = "Guardar",
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}