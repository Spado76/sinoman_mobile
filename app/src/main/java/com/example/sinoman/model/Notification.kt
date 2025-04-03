package com.example.sinoman.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date
import java.util.UUID

data class Notification(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: NotificationType
) {
    companion object {
        private const val PREF_NAME = "notification_prefs"
        private const val KEY_NOTIFICATIONS = "notifications"

        fun saveNotifications(context: Context, notifications: List<Notification>) {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = gson.toJson(notifications)
            prefs.edit().putString(KEY_NOTIFICATIONS, json).apply()
        }

        fun loadNotifications(context: Context): List<Notification> {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = prefs.getString(KEY_NOTIFICATIONS, null)
            return if (json != null) {
                val type = object : TypeToken<List<Notification>>() {}.type
                gson.fromJson(json, type)
            } else {
                emptyList()
            }
        }

        fun addNotification(context: Context, notification: Notification) {
            val notifications = loadNotifications(context).toMutableList()
            notifications.add(0, notification) // Add to the beginning of the list
            saveNotifications(context, notifications)
        }

        fun markAsRead(context: Context, notificationId: String) {
            val notifications = loadNotifications(context).toMutableList()
            val index = notifications.indexOfFirst { it.id == notificationId }
            if (index != -1) {
                val notification = notifications[index].copy(isRead = true)
                notifications[index] = notification
                saveNotifications(context, notifications)
            }
        }

        fun markAllAsRead(context: Context) {
            val notifications = loadNotifications(context).map { 
                it.copy(isRead = true) 
            }
            saveNotifications(context, notifications)
        }

        fun deleteNotification(context: Context, notificationId: String) {
            val notifications = loadNotifications(context).toMutableList()
            notifications.removeIf { it.id == notificationId }
            saveNotifications(context, notifications)
        }

        fun clearAllNotifications(context: Context) {
            saveNotifications(context, emptyList())
        }

        fun getUnreadCount(context: Context): Int {
            return loadNotifications(context).count { !it.isRead }
        }
    }
}

enum class NotificationType {
    DATA_COMPLETION,
    REGISTRATION_SUBMISSION,
    SYSTEM
}

