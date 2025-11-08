package com.example.projectedu.ui.screens.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
        containerColor = BackgroundDark
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
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
                        items(
                            items = state.filteredTasks,
                            key = { task -> task.id }
                        ) { task ->
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

            // Snackbar mejorado con animación
            state.successMessage?.let { message ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ) + fadeIn(),
                        exit = slideOutVertically(
                            targetOffsetY = { it }
                        ) + fadeOut()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    message.contains("eliminada") -> AccentRed
                                    message.contains("completada") -> AccentGreen
                                    message.contains("creada") -> PrimaryPurple
                                    message.contains("actualizada") -> AccentBlue
                                    else -> BackgroundCard
                                }
                            ),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Círculo con ícono
                                Surface(
                                    modifier = Modifier.size(48.dp),
                                    shape = CircleShape,
                                    color = TextPrimary.copy(alpha = 0.2f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = when {
                                                message.contains("eliminada") -> Icons.Default.Delete
                                                message.contains("completada") -> Icons.Default.CheckCircle
                                                message.contains("creada") -> Icons.Default.Add
                                                message.contains("actualizada") -> Icons.Default.Edit
                                                else -> Icons.Default.CheckCircle
                                            },
                                            contentDescription = null,
                                            tint = TextPrimary,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }

                                // Texto
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = when {
                                            message.contains("eliminada") -> "Tarea Eliminada"
                                            message.contains("completada") -> "¡Bien Hecho!"
                                            message.contains("creada") -> "Tarea Creada"
                                            message.contains("actualizada") -> "Tarea Actualizada"
                                            else -> "Éxito"
                                        },
                                        color = TextPrimary,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = message,
                                        color = TextPrimary.copy(alpha = 0.9f),
                                        fontSize = 14.sp
                                    )
                                }

                                // Emoji grande
                                Text(
                                    text = when {
                                        message.contains("eliminada") -> "🗑️"
                                        message.contains("completada") -> "🎉"
                                        message.contains("creada") -> "✨"
                                        message.contains("actualizada") -> "📝"
                                        else -> "✓"
                                    },
                                    fontSize = 32.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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

    // SwipeToDismiss state
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onEdit()
                    false
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete()
                    false
                }
                SwipeToDismissBoxValue.Settled -> false
            }
        },
        positionalThreshold = { it * 0.5f }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val direction = dismissState.dismissDirection
            val color = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> PrimaryPurple
                SwipeToDismissBoxValue.EndToStart -> AccentRed
                SwipeToDismissBoxValue.Settled -> androidx.compose.ui.graphics.Color.Transparent
            }

            val alignment = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                SwipeToDismissBoxValue.Settled -> Alignment.Center
            }

            val icon = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Edit
                SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                SwipeToDismissBoxValue.Settled -> Icons.Default.Done
            }

            val text = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> "Editar"
                SwipeToDismissBoxValue.EndToStart -> "Eliminar"
                SwipeToDismissBoxValue.Settled -> ""
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = text,
                        tint = TextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = text,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        enableDismissFromStartToEnd = !task.isCompleted,
        enableDismissFromEndToStart = !task.isCompleted
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (task.isCompleted) BackgroundCard.copy(alpha = 0.6f) else BackgroundCard
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { onToggleComplete() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = AccentGreen,
                            uncheckedColor = TextSecondary
                        )
                    )

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