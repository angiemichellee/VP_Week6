package com.example.week6.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week6.data.CommunityRepository
import com.example.week6.data.ProjectRepository
import com.example.week6.model.Community
import com.example.week6.model.CommunityIconType
import com.example.week6.model.Project
import com.example.week6.ui.widgets.AddProjectDialog
import com.example.week6.ui.widgets.CommunityIconBox
import com.example.week6.ui.widgets.JoinedSmallBadge
import com.example.week6.ui.widgets.ProjectStatusBadge
import com.example.week6.viewmodel.ClubProjectsUiState
import com.example.week6.viewmodel.ClubProjectsViewModel

@Composable
fun ClubProjectsScreen(
    onBackClick: () -> Unit,
    onProjectClick: (projectId: String) -> Unit,
    viewModel: ClubProjectsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ClubProjectsContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onProjectClick = onProjectClick,
        onOpenAddDialog = viewModel::openAddDialog,
        onCancelAddDialog = viewModel::closeAddDialog,
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onToggleTechStack = viewModel::onToggleTechStack,
        onSaveProject = viewModel::saveProject,
        onDeleteProject = viewModel::deleteProject
    )
}

/**
 * STATELESS: hanya menggambar UI dari state yang diterima.
 */
@Composable
fun ClubProjectsContent(
    uiState: ClubProjectsUiState,
    onBackClick: () -> Unit,
    onProjectClick: (projectId: String) -> Unit,
    onOpenAddDialog: () -> Unit,
    onCancelAddDialog: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onToggleTechStack: (String) -> Unit,
    onSaveProject: () -> Unit,
    onDeleteProject: (projectId: String) -> Unit
) {
    val community: Community? = uiState.community
    val clubName = community?.name ?: "Club"
    val clubCategory = community?.category ?: "Tech Community"
    val clubProjects = uiState.projects
    val clubIconType = community?.iconType ?: CommunityIconType.CODE

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar (sama seperti pola di Practice2 GalleryDetailScreen)
            Surface(
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color(0xFF0F172A)
                        )
                    }
                    Column {
                        Text(
                            text = "$clubName Projects",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = clubCategory,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 14.dp,
                    bottom = 96.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Community Banner Card
                item(key = "club_banner") {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFEFF6FF),
                        border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = clubCategory,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1D4ED8)
                                )
                                Text(
                                    text = "${community?.memberCount ?: 100} Anggota \u2022 ${clubProjects.size} Proyek Aktif",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF1D4ED8).copy(alpha = 0.8f)
                                )
                            }
                            JoinedSmallBadge()
                        }
                    }
                }

                // Section Header: Daftar Proyek Mahasiswa
                item(key = "club_projects_header") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Folder,
                            contentDescription = null,
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Daftar Proyek Mahasiswa (${clubProjects.size})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }
                }

                if (clubProjects.isEmpty()) {
                    item(key = "empty_club_projects") {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp, horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Belum ada proyek yang dicatat",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF64748B),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(
                        items = clubProjects,
                        key = { project -> project.id }
                    ) { project ->
                        ClubProjectItemCard(
                            project = project,
                            iconType = clubIconType,
                            onCardClick = { onProjectClick(project.id) },
                            onDeleteClick = { onDeleteProject(project.id) }
                        )
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = onOpenAddDialog,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Project"
                )
            },
            text = {
                Text(
                    text = "Add Project",
                    fontWeight = FontWeight.Bold
                )
            },
            containerColor = Color(0xFFEFF6FF),
            contentColor = Color(0xFF1D4ED8),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )

        if (uiState.isAddDialogOpen) {
            AddProjectDialog(
                clubName = clubName,
                clubCategory = clubCategory,
                title = uiState.formTitle,
                description = uiState.formDescription,
                availableTechStacks = uiState.availableTechStacks,
                selectedTechStacks = uiState.formSelectedTechStack,
                isSaveEnabled = uiState.isFormValid,
                onTitleChange = onTitleChange,
                onDescriptionChange = onDescriptionChange,
                onToggleTechStack = onToggleTechStack,
                onCancel = onCancelAddDialog,
                onSave = onSaveProject
            )
        }
    }
}

@Composable
private fun ClubProjectItemCard(
    project: Project,
    iconType: CommunityIconType,
    onCardClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CommunityIconBox(
                        iconType = iconType,
                        boxSize = 42.dp,
                        iconSize = 20.dp
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = project.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = project.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 17.sp
                        )
                    }
                }

                // Red Delete Button
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFEF2F2))
                        .clickable { onDeleteClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.DeleteOutline,
                        contentDescription = "Delete Project",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Tech Stack chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(project.techStack) { tech ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF1F5F9),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Text(
                            text = tech,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0F172A),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            HorizontalDivider(color = Color(0xFFE2E8F0).copy(alpha = 0.6f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID: ${project.id}",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF64748B)
                )
                ProjectStatusBadge(status = project.status)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClubProjectsContentPreview() {
    ClubProjectsContent(
        uiState = ClubProjectsUiState(
            community = CommunityRepository.getCommunityById("gdsc"),
            projects = ProjectRepository.getProjectList().filter { it.clubId == "gdsc" }
        ),
        onBackClick = {},
        onProjectClick = {},
        onOpenAddDialog = {},
        onCancelAddDialog = {},
        onTitleChange = {},
        onDescriptionChange = {},
        onToggleTechStack = {},
        onSaveProject = {},
        onDeleteProject = {}
    )
}
