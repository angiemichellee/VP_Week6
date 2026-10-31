package com.example.week6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week6.data.CommunityRepository
import com.example.week6.model.Community
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class CommunitiesUiState(
    val communities: List<Community> = emptyList(),
    val searchQuery: String = ""
) {
    val filteredCommunities: List<Community>
        get() = if (searchQuery.isBlank()) {
            communities
        } else {
            communities.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                    it.category.contains(searchQuery, ignoreCase = true) ||
                    it.description.contains(searchQuery, ignoreCase = true)
            }
        }
}

class CommunitiesViewModel(
    private val communityRepository: CommunityRepository = CommunityRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<CommunitiesUiState> = combine(
        communityRepository.communities,
        _searchQuery
    ) { communities, query ->
        CommunitiesUiState(
            communities = communities,
            searchQuery = query
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CommunitiesUiState(
            communities = communityRepository.getCommunityList()
        )
    )

    fun toggleJoin(clubId: String) {
        communityRepository.toggleJoin(clubId)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
}
