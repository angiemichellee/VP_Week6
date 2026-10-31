package com.example.week6.model

enum class CommunityIconType {
    CODE,
    SECURITY,
    AI,
    MOBILE,
    CLOUD,
    IOT
}

data class Community(
    val id: String,
    val name: String,
    val category: String,
    val memberCount: Int,
    val description: String,
    val iconType: CommunityIconType,
    val isJoined: Boolean = false
)
