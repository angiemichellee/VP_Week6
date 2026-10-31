package com.example.week6.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.week6.data.CommunityRepository
import com.example.week6.data.ProjectRepository
import com.example.week6.model.Community
import com.example.week6.model.Project
import com.example.week6.navigation.ClubProjectsRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

private data class ProjectFormState(
    val isAddDialogOpen: Boolean = false,
    val title: String = "",
    val description: String = "",
    val selectedTechStack: List<String> = emptyList()
)

data class ClubProjectsUiState(
    val community: Community? = null,
    val projects: List<Project> = emptyList(),
    val availableTechStacks: List<String> = ProjectRepository.availableTechStacks,
    val isAddDialogOpen: Boolean = false,
    val formTitle: String = "",
    val formDescription: String = "",
    val formSelectedTechStack: List<String> = emptyList()
) {
    val isFormValid: Boolean
        get() = formTitle.trim().isNotEmpty() &&
            formDescription.trim().isNotEmpty() &&
            formSelectedTechStack.isNotEmpty()
}

class ClubProjectsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val clubId: String = savedStateHandle.toRoute<ClubProjectsRoute>().clubId
    private val _formState = MutableStateFlow(ProjectFormState())

    val uiState: StateFlow<ClubProjectsUiState> = combine(
        CommunityRepository.communities,
        ProjectRepository.projects,
        _formState
    ) { communities, projects, form ->
        ClubProjectsUiState(
            community = communities.firstOrNull { it.id == clubId },
            projects = projects.filter { it.clubId == clubId },
            availableTechStacks = ProjectRepository.availableTechStacks,
            isAddDialogOpen = form.isAddDialogOpen,
            formTitle = form.title,
            formDescription = form.description,
            formSelectedTechStack = form.selectedTechStack
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ClubProjectsUiState(
            community = CommunityRepository.getCommunityById(clubId),
            projects = ProjectRepository.getProjectList().filter { it.clubId == clubId }
        )
    )

    fun openAddDialog() {
        _formState.update {
            it.copy(
                isAddDialogOpen = true,
                title = "",
                description = "",
                selectedTechStack = emptyList()
            )
        }
    }

    fun closeAddDialog() {
        _formState.update {
            it.copy(
                isAddDialogOpen = false,
                title = "",
                description = "",
                selectedTechStack = emptyList()
            )
        }
    }

    fun onTitleChange(value: String) {
        _formState.update { it.copy(title = value) }
    }

    fun onDescriptionChange(value: String) {
        _formState.update { it.copy(description = value) }
    }

    fun onToggleTechStack(tech: String) {
        _formState.update { state ->
            val updated = if (tech in state.selectedTechStack) {
                state.selectedTechStack - tech
            } else {
                state.selectedTechStack + tech
            }
            state.copy(selectedTechStack = updated)
        }
    }

    fun saveProject() {
        val form = _formState.value
        if (form.title.isBlank() || form.description.isBlank() || form.selectedTechStack.isEmpty()) {
            return
        }
        val clubName = CommunityRepository.getCommunityById(clubId)?.name ?: clubId.uppercase()
        ProjectRepository.add(
            clubId = clubId,
            clubName = clubName,
            title = form.title,
            description = form.description,
            techStack = form.selectedTechStack
        )
        closeAddDialog()
    }

    fun deleteProject(projectId: String) {
        ProjectRepository.deleteById(projectId)
    }
}
