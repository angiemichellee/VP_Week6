package com.example.week6.data

import com.example.week6.model.Community
import com.example.week6.model.CommunityIconType
import com.example.week6.model.StudentProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object CommunityRepository {

    val studentProfile = StudentProfile(
        initials = ":)",
        name = "Michelleeee",
        major = "Teknik Informatika",
        batch = "Angkatan 2025",
        nim = "22001048",
        statusBadge = "MAHASISWA AKTIF BANGET"
    )

    private val initialCommunities = listOf(
        Community(
            id = "gdsc",
            name = "GDSC",
            category = "Google Developer Student Club",
            memberCount = 142,
            description = "Fokus pada ekosistem Google: Android Compose, Flutter, Firebase, dan Google Cloud Platform.",
            iconType = CommunityIconType.CODE,
            isJoined = true
        ),
        Community(
            id = "cyber",
            name = "CyberSecurity Guild",
            category = "Security & Pentesting",
            memberCount = 88,
            description = "Eksplorasi ethical hacking, CTF challenges, reverse engineering, dan pertahanan jaringan kampus.",
            iconType = CommunityIconType.SECURITY,
            isJoined = true
        ),
        Community(
            id = "ai",
            name = "AI Society",
            category = "Machine Learning & LLM",
            memberCount = 115,
            description = "Riset terapan deep learning, computer vision, agen cerdas, dan fine-tuning large language models.",
            iconType = CommunityIconType.AI,
            isJoined = false
        ),
        Community(
            id = "mobile",
            name = "Mobile Dev Community",
            category = "Jetpack Compose & KMP",
            memberCount = 96,
            description = "Native Jetpack Compose & iOS, pengembangan aplikasi lintas platform dengan Kotlin Multiplatform.",
            iconType = CommunityIconType.MOBILE,
            isJoined = true
        ),
        Community(
            id = "cloud",
            name = "Cloud & Backend Guild",
            category = "Cloud Architecture & Ktor",
            memberCount = 104,
            description = "Pengembangan microservice menggunakan Ktor, containerization Docker, dan infrastruktur cloud.",
            iconType = CommunityIconType.CLOUD,
            isJoined = false
        ),
        Community(
            id = "iot",
            name = "IoT & Robotics Club",
            category = "Embedded Systems & Sensor",
            memberCount = 74,
            description = "Eksperimen sistem tertanam, otomasi sensor pintar kampus, dan integrasi mikrokontroler.",
            iconType = CommunityIconType.IOT,
            isJoined = false
        )
    )

    private val _communities = MutableStateFlow(initialCommunities)
    val communities: StateFlow<List<Community>> = _communities.asStateFlow()

    fun getCommunityList(): List<Community> = _communities.value

    fun getCommunityById(clubId: String): Community? =
        _communities.value.firstOrNull { it.id == clubId }

    fun toggleJoin(clubId: String) {
        _communities.update { list ->
            list.map { community ->
                if (community.id == clubId) {
                    community.copy(isJoined = !community.isJoined)
                } else {
                    community
                }
            }
        }
    }
}
