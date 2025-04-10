package com.example.sinoman.model

data class FaqItem(
    val id: String,
    val question: String,
    val answer: String,
    var isExpanded: Boolean = false
)
