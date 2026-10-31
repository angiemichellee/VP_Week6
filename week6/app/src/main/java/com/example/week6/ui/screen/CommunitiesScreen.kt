package com.example.week6.ui.screen

import android.widget.Toast
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week6.data.CommunityRepository
import com.example.week6.model.Community
import com.example.week6.ui.widgets.CommunityIconBox
import com.example.week6.viewmodel.CommunitiesUiState
import com.example.week6.viewmodel.CommunitiesViewModel
import kotlinx.coroutines.launch

private const val GATE_BLOCKED_MESSAGE = "Harap bergabung ke komunitas terlebih dahulu!"

@Composable
fun CommunitiesScreen(
    onOpenClubProjects: (clubId: String) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    viewModel: CommunitiesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var isSearchVisible by rememberSaveable { mutableStateOf(false) }

    CommunitiesContent(
        uiState = uiState,
        listState = listState,
        isSearchVisible = isSearchVisible,
        snackbarHostState = snackbarHostState,
        onToggleSearch = {
            if (isSearchVisible) {
                viewModel.onSearchQueryChange("")
            }
            isSearchVisible = !isSearchVisible
        },
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onToggleJoin = viewModel::toggleJoin,
        onCommunityCardClick = { community ->
            if (community.isJoined) {
                onOpenClubProjects(community.id)
            } else {
                Toast.makeText(context, GATE_BLOCKED_MESSAGE, Toast.LENGTH_SHORT).show()
                coroutineScope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = GATE_BLOCKED_MESSAGE,
                        actionLabel = "OK",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    )
}

/**
 * STATELESS: hanya menggambar UI dari state yang diterima.
 */
@Composable
fun CommunitiesContent(
    uiState: CommunitiesUiState,
    listState: LazyListState = rememberLazyListState(),
    isSearchVisible: Boolean = false,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onToggleSearch: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onToggleJoin: (clubId: String) -> Unit,
    onCommunityCardClick: (Community) -> Unit
) {
    val communities = uiState.filteredCommunities

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 14.dp,
                bottom = 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            item(key = "communities_header") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Communities Directory",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Pilih komunitas untuk membuka Club Projects",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B)
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            IconButton(
                                onClick = onToggleSearch,
                                modifier = Modifier.size(38.dp)
                            ) {
                                Icon(
                                    imageVector = if (isSearchVisible) Icons.Filled.Close else Icons.Filled.Search,
                                    contentDescription = "Search Communities",
                                    tint = Color(0xFF0F172A),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    if (isSearchVisible) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = onSearchQueryChange,
                            placeholder = { Text("Cari nama atau bidang komunitas...") },
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null,
                                    tint = Color(0xFF64748B)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF0F172A),
                                unfocusedTextColor = Color(0xFF0F172A),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                cursorColor = Color(0xFF1D4ED8),
                                focusedBorderColor = Color(0xFF1D4ED8),
                                unfocusedBorderColor = Color(0xFFCBD5E1)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            items(
                items = communities,
                key = { community -> community.id }
            ) { community ->
                CommunityDirectoryCard(
                    community = community,
                    onCardClick = { onCommunityCardClick(community) },
                    onToggleJoin = { onToggleJoin(community.id) }
                )
            }
        }

        // Custom styled Snackbar matching screenshot at bottom
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) { data ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF0F172A),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFD97706)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = data.visuals.message,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    data.visuals.actionLabel?.let { label ->
                        TextButton(onClick = { data.performAction() }) {
                            Text(
                                text = label,
                                color = Color(0xFF60A5FA),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CommunityDirectoryCard(
    community: Community,
    onCardClick: () -> Unit,
    onToggleJoin: () -> Unit
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CommunityIconBox(
                    iconType = community.iconType,
                    boxSize = 46.dp,
                    iconSize = 22.dp
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = community.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "${community.category} \u2022 ${community.memberCount} Mahasiswa",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Text(
                text = community.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B),
                fontSize = 13.sp,
                lineHeight = 19.sp
            )

            HorizontalDivider(color = Color(0xFFE2E8F0).copy(alpha = 0.7f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Gate Access indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.clickable { onCardClick() }
                ) {
                    Icon(
                        imageVector = if (community.isJoined) {
                            Icons.Filled.LockOpen
                        } else {
                            Icons.Filled.Lock
                        },
                        contentDescription = null,
                        tint = if (community.isJoined) Color(0xFF1D4ED8) else Color(0xFF64748B),
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = if (community.isJoined) {
                            "Unlocked \u2022 Buka Proyek \u2192"
                        } else {
                            "Terkunci (Gate Akses)"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (community.isJoined) Color(0xFF1D4ED8) else Color(0xFF64748B)
                    )
                }

                // Right Join / Joined Toggle Button
                if (community.isJoined) {
                    OutlinedButton(
                        onClick = onToggleJoin,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color(0xFFFECACA)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFFEF2F2),
                            contentColor = Color(0xFFDC2626)
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = "\u2713 Joined",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = onToggleJoin,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1D4ED8),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = "+ Join",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommunitiesContentPreview() {
    CommunitiesContent(
        uiState = CommunitiesUiState(
            communities = CommunityRepository.getCommunityList()
        ),
        onToggleSearch = {},
        onSearchQueryChange = {},
        onToggleJoin = {},
        onCommunityCardClick = {}
    )
}
