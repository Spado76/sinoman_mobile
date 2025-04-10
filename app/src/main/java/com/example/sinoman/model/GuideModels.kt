package com.example.sinoman.model

data class GuideSection(
    val id: String,
    val title: String,
    val iconResId: Int,
    val steps: List<GuideStep>,
    var isExpanded: Boolean = false
)

data class GuideStep(
    val number: Int,
    val title: String,
    val description: String
)
