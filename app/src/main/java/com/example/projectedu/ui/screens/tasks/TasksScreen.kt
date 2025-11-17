package com.example.projectedu.ui.screens.tasks

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectedu.data.model.Subtask
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
                // Barra de progreso SEMANAL
                WeeklyTaskProgressCard(
                    completedTasks = viewModel.getCompletedWeeklyTasksCount(),
                    totalTasks = viewModel.getTotalWeeklyTasksCount(),
                    progress = viewModel.getWeeklyTaskCompletionPercentage()
                )

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
                            TaskItemCardWithProgress(
                                task = task,
                                onToggleComplete = { viewModel.onToggleTaskCompletion(task) },
                                onEdit = { viewModel.onShowEditDialog(task) },
                                onDelete = { viewModel.onShowDeleteDialog(task) },
                                onToggleSubtask = { subtask -> viewModel.onToggleSubtask(task, subtask) },
                                onManageSubtasks = { viewModel.onShowSubtasksDialog(task) }
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
                    notes = state.editNotes,
                    subject = state.editSubject,
                    priority = state.editPriority,
                    type = state.editType,
                    dueDate = state.editDueDate,
                    onTitleChange = { viewModel.onTitleChange(it) },
                    onDescriptionChange = { viewModel.onDescriptionChange(it) },
                    onNotesChange = { viewModel.onNotesChange(it) },
                    onSubjectChange = { viewModel.onSubjectChange(it) },
                    onPriorityChange = { viewModel.onPriorityChange(it) },
                    onTypeChange = { viewModel.onTypeChange(it) },
                    onDateClick = { viewModel.onShowDatePicker() },
                    onTimeClick = { viewModel.onShowTimePicker() },
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
                    notes = state.editNotes,
                    subject = state.editSubject,
                    priority = state.editPriority,
                    type = state.editType,
                    dueDate = state.editDueDate,
                    onTitleChange = { viewModel.onTitleChange(it) },
                    onDescriptionChange = { viewModel.onDescriptionChange(it) },
                    onNotesChange = { viewModel.onNotesChange(it) },
                    onSubjectChange = { viewModel.onSubjectChange(it) },
                    onPriorityChange = { viewModel.onPriorityChange(it) },
                    onTypeChange = { viewModel.onTypeChange(it) },
                    onDateClick = { viewModel.onShowDatePicker() },
                    onTimeClick = { viewModel.onShowTimePicker() },
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

            // Dialog de gestión de subtareas
            if (state.showSubtasksDialog) {
                SubtasksDialog(
                    task = state.selectedTask,
                    subtasks = state.editSubtasks,
                    newSubtaskTitle = state.newSubtaskTitle,
                    onNewSubtaskTitleChange = { viewModel.onNewSubtaskTitleChange(it) },
                    onAddSubtask = { viewModel.onAddSubtask() },
                    onRemoveSubtask = { viewModel.onRemoveSubtask(it) },
                    onDismiss = { viewModel.onDismissDialogs() },
                    onSave = { viewModel.onSaveSubtasks() }
                )
            }

            // DatePicker
            if (state.showDatePicker) {
                val context = LocalContext.current
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = state.editDueDate

                LaunchedEffect(Unit) {
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            viewModel.onDateSelected(year, month, day)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).apply {
                        setOnDismissListener { viewModel.onDismissDialogs() }
                        show()
                    }
                }
            }

            // TimePicker
            if (state.showTimePicker) {
                val context = LocalContext.current
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = state.editDueDate

                LaunchedEffect(Unit) {
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            viewModel.onTimeSelected(hour, minute)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).apply {
                        setOnDismissListener { viewModel.onDismissDialogs() }
                        show()
                    }
                }
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

@Composable
fun WeeklyTaskProgressCard(
    completedTasks: Int,
    totalTasks: Int,
    progress: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundCard
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📅",
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Progreso de Esta Semana",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = "$completedTasks de $totalTasks tareas completadas",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = PrimaryPurple.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )
                }
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = PrimaryPurple,
                trackColor = XPBarBackground
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItemCardWithProgress(
    task: Task,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleSubtask: (Subtask) -> Unit,
    onManageSubtasks: () -> Unit
) {
    val priorityColor = when (task.priority) {
        "Alta" -> PriorityHigh
        "Media" -> PriorityMedium
        else -> PriorityLow
    }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    var expanded by remember { mutableStateOf(false) }

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
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            colors = CardDefaults.cardColors(
                containerColor = if (task.isCompleted) BackgroundCard.copy(alpha = 0.6f) else BackgroundCard
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
                                    maxLines = if (expanded) Int.MAX_VALUE else 2
                                )
                            }

                            if (task.notes.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notes,
                                        contentDescription = "Notas",
                                        tint = TextTertiary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = task.notes,
                                        fontSize = 12.sp,
                                        color = TextTertiary,
                                        maxLines = if (expanded) Int.MAX_VALUE else 1
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Barra de progreso de subtareas
                            if (task.subtasks.isNotEmpty()) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${task.getCompletedSubtasksCount()}/${task.subtasks.size} subtareas",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                        Text(
                                            text = "${(task.getSubtaskProgress() * 100).toInt()}%",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = PrimaryPurple
                                        )
                                    }

                                    LinearProgressIndicator(
                                        progress = { task.getSubtaskProgress() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = if (task.areAllSubtasksCompleted()) AccentGreen else PrimaryPurple,
                                        trackColor = XPBarBackground
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                            }

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

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
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

                        IconButton(
                            onClick = { expanded = !expanded },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (expanded) "Contraer" else "Expandir",
                                tint = TextSecondary
                            )
                        }
                    }
                }

                // Subtareas expandidas
                if (expanded && task.subtasks.isNotEmpty()) {
                    Divider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = SurfaceBorder
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Subtareas:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )

                            TextButton(onClick = onManageSubtasks) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Gestionar",
                                    modifier = Modifier.size(16.dp),
                                    tint = PrimaryPurple
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Gestionar", color = PrimaryPurple, fontSize = 12.sp)
                            }
                        }

                        task.subtasks.forEach { subtask ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onToggleSubtask(subtask) }
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = subtask.isCompleted,
                                    onCheckedChange = { onToggleSubtask(subtask) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = AccentGreen,
                                        uncheckedColor = TextSecondary
                                    ),
                                    modifier = Modifier.size(20.dp)
                                )

                                Text(
                                    text = subtask.title,
                                    fontSize = 14.sp,
                                    color = if (subtask.isCompleted) TextDisabled else TextSecondary,
                                    style = if (subtask.isCompleted) {
                                        androidx.compose.ui.text.TextStyle(
                                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                        )
                                    } else {
                                        androidx.compose.ui.text.TextStyle()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubtasksDialog(
    task: Task?,
    subtasks: List<Subtask>,
    newSubtaskTitle: String,
    onNewSubtaskTitleChange: (String) -> Unit,
    onAddSubtask: () -> Unit,
    onRemoveSubtask: (Subtask) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
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
                    text = "Gestionar Subtareas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = task?.title ?: "",
                    fontSize = 16.sp,
                    color = TextSecondary
                )

                Divider(color = SurfaceBorder)

                // Agregar nueva subtarea
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomTextField(
                        value = newSubtaskTitle,
                        onValueChange = onNewSubtaskTitleChange,
                        label = "Nueva subtarea",
                        leadingIcon = Icons.Default.Add,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onAddSubtask,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = PrimaryPurple
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Agregar",
                                tint = TextPrimary,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                // Lista de subtareas
                if (subtasks.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "📝",
                                fontSize = 48.sp
                            )
                            Text(
                                text = "No hay subtareas",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(subtasks) { subtask ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(BackgroundDark, RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = subtask.title,
                                    fontSize = 14.sp,
                                    color = TextPrimary,
                                    modifier = Modifier.weight(1f)
                                )

                                IconButton(
                                    onClick = { onRemoveSubtask(subtask) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = AccentRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
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
                        onClick = onSave,
                        modifier = Modifier.weight(1f)
                    )
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
    notes: String,
    subject: String,
    priority: String,
    type: String,
    dueDate: Long,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSubjectChange: (String) -> Unit,
    onPriorityChange: (String) -> Unit,
    onTypeChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isLoading: Boolean
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val date = Date(dueDate)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 700.dp),
            colors = CardDefaults.cardColors(
                containerColor = BackgroundCard
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
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
                    value = notes,
                    onValueChange = onNotesChange,
                    label = "Notas adicionales",
                    leadingIcon = Icons.Default.Notes
                )

                CustomTextField(
                    value = subject,
                    onValueChange = onSubjectChange,
                    label = "Materia",
                    leadingIcon = Icons.Default.Book
                )

                // Fecha y Hora
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onDateClick() },
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = BackgroundDark
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Fecha",
                                tint = PrimaryPurple,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = "Fecha",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = dateFormat.format(date),
                                    fontSize = 14.sp,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    OutlinedCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onTimeClick() },
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = BackgroundDark
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Hora",
                                tint = PrimaryPurple,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = "Hora",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = timeFormat.format(date),
                                    fontSize = 14.sp,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

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