package com.example.week6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week6.data.CommunityRepository
import com.example.week6.data.ProjectRepository
import com.example.week6.model.Community
import com.example.week6.model.Project
import com.example.week6.model.StudentProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DashboardUiState(
    val studentProfile: StudentProfile = CommunityRepository.studentProfile,
    val communities: List<Community> = emptyList(),
    val projects: List<Project> = emptyList()
) {
    val joinedCommunities: List<Community>
        get() = communities.filter { it.isJoined }

    val joinedClubsCount: Int
        get() = joinedCommunities.size

    val totalProjectsCount: Int
        get() = projects.size

    fun projectsForClub(clubId: String): List<Project> =
        projects.filter { it.clubId == clubId }

    fun getCommunity(clubId: String): Community? =
        communities.firstOrNull { it.id == clubId }
}

class DashboardViewModel(
    private val communityRepository: CommunityRepository = CommunityRepository,
    private val projectRepository: ProjectRepository = ProjectRepository
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        communityRepository.communities,
        projectRepository.projects
    ) { communities, projects ->
        DashboardUiState(
            studentProfile = communityRepository.studentProfile,
            communities = communities,
            projects = projects
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState(
            communities = communityRepository.getCommunityList(),
            projects = projectRepository.getProjectList()
        )
    )
}
