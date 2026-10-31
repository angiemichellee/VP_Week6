package com.example.week6.data

import com.example.week6.model.Project
import com.example.week6.model.ProjectStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object ProjectRepository {

    val availableTechStacks = listOf(
        "Kotlin",
        "Jetpack Compose",
        "Room",
        "Firebase",
        "Ktor",
        "Python",
        "Google Maps SDK",
        "CameraX"
    )

    private val initialProjects = listOf(
        Project(
            id = "proj-gdsc-01",
            clubId = "gdsc",
            clubName = "GDSC",
            title = "Smart Campus Navigation",
            description = "Sistem navigasi peta gedung kampus interaktif berbasis Jetpack Compose dan Google Maps SDK. Memfasilitasi mahasiswa dan dosen menemukan ruang perkuliahan, laboratorium komputer, perpustakaan, serta fasilitas umum kampus secara akurat disertai rute tercepat dan visualisasi lokasi real-time.",
            techStack = listOf("Kotlin", "Jetpack Compose", "Room", "Google Maps SDK"),
            status = ProjectStatus.IN_PROGRESS
        ),
        Project(
            id = "proj-gdsc-02",
            clubId = "gdsc",
            clubName = "GDSC",
            title = "Student Attendance QR Scanner",
            description = "Pemindai barcode & QR presensi kehadiran workshop berbasis CameraX & Firebase dengan rekapitulasi otomatis untuk pengurus komunitas.",
            techStack = listOf("Jetpack Compose", "Firebase", "CameraX"),
            status = ProjectStatus.COMPLETED
        ),
        Project(
            id = "proj-cyber-01",
            clubId = "cyber",
            clubName = "CyberSecurity Guild",
            title = "Campus Lab Vulnerability Scanner",
            description = "Sistem pemindaian celah keamanan jaringan laboratorium kampus dan otomatisasi laporan audit keamanan berkala berbasis Python dan Ktor.",
            techStack = listOf("Python", "Ktor", "Firebase"),
            status = ProjectStatus.IN_PROGRESS
        ),
        Project(
            id = "proj-mobile-01",
            clubId = "mobile",
            clubName = "Mobile Dev Community",
            title = "Developer RSVP & Study Portal",
            description = "Portal pendaftaran webinar dan bootcamp teknologi sinkronisasi Ktor API server dengan penyimpanan lokal Room.",
            techStack = listOf("Kotlin", "Jetpack Compose", "Ktor", "Room"),
            status = ProjectStatus.COMPLETED
        )
    )

    private var nextSequence = 4

    private val _projects = MutableStateFlow(initialProjects)
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    fun getProjectList(): List<Project> = _projects.value

    fun getProjectById(projectId: String): Project? =
        _projects.value.firstOrNull { it.id == projectId }

    fun add(
        clubId: String,
        clubName: String,
        title: String,
        description: String,
        techStack: List<String>
    ) {
        val clubCount = _projects.value.count { it.clubId == clubId } + 1
        val seqNum = maxOf(clubCount, nextSequence++)
        val id = "proj-${clubId.lowercase()}-${seqNum.toString().padStart(2, '0')}"
        val newProject = Project(
            id = id,
            clubId = clubId,
            clubName = clubName,
            title = title.trim(),
            description = description.trim(),
            techStack = techStack,
            status = ProjectStatus.IN_PROGRESS
        )
        _projects.update { it + newProject }
    }

    fun deleteById(projectId: String) {
        _projects.update { list ->
            list.filterNot { it.id == projectId }
        }
    }

    fun toggleStatus(projectId: String) {
        _projects.update { list ->
            list.map { project ->
                if (project.id == projectId) {
                    val newStatus = if (project.status == ProjectStatus.IN_PROGRESS) {
                        ProjectStatus.COMPLETED
                    } else {
                        ProjectStatus.IN_PROGRESS
                    }
                    project.copy(status = newStatus)
                } else {
                    project
                }
            }
        }
    }

    fun setStatus(projectId: String, isCompleted: Boolean) {
        _projects.update { list ->
            list.map { project ->
                if (project.id == projectId) {
                    project.copy(
                        status = if (isCompleted) ProjectStatus.COMPLETED else ProjectStatus.IN_PROGRESS
                    )
                } else {
                    project
                }
            }
        }
    }
}
