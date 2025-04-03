package com.example.sinoman.utils

import android.content.Context
import com.example.sinoman.FormData
import com.example.sinoman.RegistrationData
import com.example.sinoman.RegistrationStatus
import com.example.sinoman.RegistrationType
import com.example.sinoman.model.Notification
import com.example.sinoman.model.NotificationType

object NotificationManager {
    
    // Check for data completion and create notification if needed
    fun checkDataCompletion(context: Context) {
        val formData = FormData.load(context)
        
        // Check if all personal data is complete and create notification
        if (FormData.isPersonalDataComplete(formData)) {
            // Check if notification already exists to avoid duplicates
            val notifications = Notification.loadNotifications(context)
            val exists = notifications.any { 
                it.type == NotificationType.DATA_COMPLETION && 
                it.title == "Data Pribadi Lengkap" 
            }
            
            if (!exists) {
                val notification = Notification(
                    title = "Data Pribadi Lengkap",
                    message = "Selamat! Anda telah melengkapi semua data anda!",
                    type = NotificationType.DATA_COMPLETION
                )
                Notification.addNotification(context, notification)
            }
        }
    }
    
    // Check for registration submissions and create notifications
    fun checkRegistrationSubmissions(context: Context) {
        val registrations = RegistrationData.loadRegistrations(context)
        
        // Check for new submissions (status changed to UNDER_REVIEW)
        registrations.forEach { registration ->
            if (registration.status == RegistrationStatus.UNDER_REVIEW) {
                // Check if notification already exists for this registration
                val notifications = Notification.loadNotifications(context)
                val exists = notifications.any { 
                    it.type == NotificationType.REGISTRATION_SUBMISSION && 
                    it.message.contains(registration.id) 
                }
                
                if (!exists) {
                    val title = when (registration.type) {
                        RegistrationType.HOUSE_OWNER -> "Pendaftaran Bantuan Punya Rumah"
                        RegistrationType.NON_HOUSE_OWNER -> "Pendaftaran Bantuan Tidak Punya Rumah"
                    }
                    
                    val notification = Notification(
                        title = title,
                        message = "$title anda telah terkirim dan sedang ditinjau! (ID: ${registration.id})",
                        type = NotificationType.REGISTRATION_SUBMISSION
                    )
                    Notification.addNotification(context, notification)
                }
            }
        }
    }
    
    // This method should be called when a registration is submitted
    fun notifyRegistrationSubmitted(context: Context, registration: RegistrationData) {
        val title = when (registration.type) {
            RegistrationType.HOUSE_OWNER -> "Pendaftaran Bantuan Punya Rumah"
            RegistrationType.NON_HOUSE_OWNER -> "Pendaftaran Bantuan Tidak Punya Rumah"
        }
        
        val notification = Notification(
            title = title,
            message = "$title anda telah terkirim dan sedang ditinjau!",
            type = NotificationType.REGISTRATION_SUBMISSION
        )
        Notification.addNotification(context, notification)
    }
}

