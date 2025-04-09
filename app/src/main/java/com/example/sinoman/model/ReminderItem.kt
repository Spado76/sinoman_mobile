package com.example.sinoman.model

data class ReminderItem(
    val id: String,
    val title: String,
    val message: String,
    val actionText: String,
    val type: ReminderType
)

enum class ReminderType {
    INCOMPLETE_DATA,
    PENDING_SUBMISSION,
    VERIFICATION_NEEDED
}
