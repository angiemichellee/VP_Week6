package com.example.week6.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.week6.ui.screen.ClubProjectsScreen
import com.example.week6.ui.screen.CommunitiesScreen
import com.example.week6.ui.screen.DashboardScreen
import com.example.week6.ui.screen.ProjectDetailScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isDashboard = currentDestination?.hasRoute<DashboardRoute>() == true
    val isCommunities = currentDestination?.hasRoute<CommunitiesRoute>() == true
    val showBottomBar = isDashboard || isCommunities

    // Retain scroll states across tab switches
    val dashboardListState = rememberLazyListState()
    val communitiesListState = rememberLazyListState()

    Column(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = DashboardRoute,
            modifier = Modifier.weight(1f)
        ) {
            composable<DashboardRoute> {
                DashboardScreen(
                    listState = dashboardListState,
                    onClubClick = { clubId ->
                        navController.navigate(ClubProjectsRoute(clubId)) {
                            launchSingleTop = true
                        }
                    },
                    onProjectClick = { projectId ->
                        navController.navigate(ProjectDetailRoute(projectId)) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<CommunitiesRoute> {
                CommunitiesScreen(
                    listState = communitiesListState,
                    onOpenClubProjects = { clubId ->
                        navController.navigate(ClubProjectsRoute(clubId)) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<ClubProjectsRoute> {
                ClubProjectsScreen(
                    onBackClick = { navController.popBackStack() },
                    onProjectClick = { projectId ->
                        navController.navigate(ProjectDetailRoute(projectId)) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<ProjectDetailRoute> {
                ProjectDetailScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        if (showBottomBar) {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = isDashboard,
                    onClick = {
                        if (!isDashboard) {
                            navController.navigate(DashboardRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isDashboard) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Dashboard"
                        )
                    },
                    label = {
                        Text(
                            text = "Dashboard",
                            fontWeight = if (isDashboard) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1D4ED8),
                        selectedTextColor = Color(0xFF1D4ED8),
                        indicatorColor = Color(0xFFEFF6FF),
                        unselectedIconColor = Color(0xFF64748B),
                        unselectedTextColor = Color(0xFF64748B)
                    )
                )

                NavigationBarItem(
                    selected = isCommunities,
                    onClick = {
                        if (!isCommunities) {
                            navController.navigate(CommunitiesRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isCommunities) Icons.Filled.Groups else Icons.Outlined.Groups,
                            contentDescription = "Communities"
                        )
                    },
                    label = {
                        Text(
                            text = "Communities",
                            fontWeight = if (isCommunities) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1D4ED8),
                        selectedTextColor = Color(0xFF1D4ED8),
                        indicatorColor = Color(0xFFEFF6FF),
                        unselectedIconColor = Color(0xFF64748B),
                        unselectedTextColor = Color(0xFF64748B)
                    )
                )
            }
        }
    }
}
