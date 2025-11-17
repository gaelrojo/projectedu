package com.example.projectedu.ui.screens.subjects

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectedu.data.model.Subject
import com.example.projectedu.ui.components.common.TopBar
import com.example.projectedu.ui.theme.*
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(
    navController: NavController,
    onMenuClick: () -> Unit,
    viewModel: SubjectsViewModel = viewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = PrimaryPurple,
                        contentColor = TextPrimary
                    )
                }
            )
        },
        topBar = {
            TopBar(
                title = "Mis Materias",
                onMenuClick = onMenuClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddDialog() },
                containerColor = PrimaryPurple,
                contentColor = TextPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar materia")
            }
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = PrimaryPurple
                )
            } else if (state.subjects.isEmpty()) {
                EmptySubjectsView(
                    onAddClick = { viewModel.showAddDialog() }
                )
            } else {
                SubjectsList(
                    subjects = state.subjects,
                    onEdit = { viewModel.showEditDialog(it) },
                    onDelete = {
                        viewModel.deleteSubject(it.id)
                        scope.launch {
                            snackbarHostState.showSnackbar("Materia eliminada")
                        }
                    }
                )
            }
        }

        if (state.showAddDialog) {
            AddEditSubjectDialog(
                subject = state.editingSubject,
                onDismiss = { viewModel.hideDialog() },
                onSave = { subject ->
                    if (state.editingSubject != null) {
                        viewModel.updateSubject(subject)
                    } else {
                        viewModel.addSubject(subject)
                    }
                }
            )
        }
    }
}

@Composable
private fun SubjectsList(
    subjects: List<Subject>,
    onEdit: (Subject) -> Unit,
    onDelete: (Subject) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(subjects, key = { it.id }) { subject ->
            SubjectCard(
                subject = subject,
                onEdit = { onEdit(subject) },
                onDelete = { onDelete(subject) }
            )
        }
    }
}

@Composable
private fun SubjectCard(
    subject: Subject,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val editAction = SwipeAction(
        icon = {
            Icon(
                Icons.Default.Edit,
                contentDescription = "Editar",
                tint = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        },
        background = Color(0xFF4A90E2),
        onSwipe = onEdit
    )

    val deleteAction = SwipeAction(
        icon = {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Eliminar",
                tint = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        },
        background = Color(0xFFFF5252),
        onSwipe = onDelete
    )

    SwipeableActionsBox(
        startActions = listOf(editAction),
        endActions = listOf(deleteAction),
        swipeThreshold = 100.dp
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(subject.colorValue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Book,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subject.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    if (subject.professor.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = subject.professor,
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    if (subject.schedule.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = subject.schedule,
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    if (subject.classroom.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = subject.classroom,
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptySubjectsView(
    onAddClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Book,
            contentDescription = null,
            tint = TextSecondary.copy(alpha = 0.5f),
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No tienes materias",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Agrega tus materias para organizar mejor tus tareas",
            fontSize = 16.sp,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Agregar Materia")
        }
    }
}