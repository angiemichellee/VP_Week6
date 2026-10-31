package com.example.week6.model

data class StudentProfile(
    val initials: String,
    val name: String,
    val major: String,
    val batch: String,
    val nim: String,
    val statusBadge: String = "MAHASISWA AKTIF"
)
