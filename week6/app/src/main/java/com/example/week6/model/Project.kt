package com.example.week6.model

enum class ProjectStatus(val label: String) {
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed")
}

data class Project(
    val id: String,
    val clubId: String,
    val clubName: String,
    val title: String,
    val description: String,
    val techStack: List<String>,
    val status: ProjectStatus = ProjectStatus.IN_PROGRESS
)
