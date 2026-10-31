package com.example.week6.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.week6.data.ProjectRepository
import com.example.week6.model.Project
import com.example.week6.navigation.ProjectDetailRoute
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ProjectDetailUiState(
    val project: Project? = null
)

class ProjectDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val projectId: String = savedStateHandle.toRoute<ProjectDetailRoute>().projectId

    val uiState: StateFlow<ProjectDetailUiState> = ProjectRepository.projects
        .map { projects ->
            ProjectDetailUiState(
                project = projects.firstOrNull { it.id == projectId }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProjectDetailUiState(
                project = ProjectRepository.getProjectById(projectId)
            )
        )

    fun onStatusChanged(isCompleted: Boolean) {
        ProjectRepository.setStatus(projectId, isCompleted)
    }
}
