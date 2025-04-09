package com.example.sinoman

object AuthUtils {
    // Static function to check admin credentials
    fun isAdminUser(email: String, password: String): Boolean {
        return email == "admin@gmail.com" && password == "admin"
    }
    
    // This would typically connect to a backend service
    // For this example, we'll just use the admin check
    fun loginUser(email: String, password: String): Boolean {
        return isAdminUser(email, password)
    }
    
    // In a real app, this would create a user in your backend
    fun registerUser(email: String, name: String, phone: String, password: String): Boolean {
        // For this example, we'll just return true to simulate successful registration
        return true
    }
    // Get user data for the logged in user
    fun getUserData(email: String): UserData {
        // For this example, we'll return hardcoded data for the admin user
        return if (email == "admin@gmail.com") {
            UserData(
                name = "Admin Firman",
                email = email,
                phone = "012345"
            )
        } else {
            UserData(
                name = "User",
                email = email,
                phone = ""
            )
        }
    }
}

// Data class to hold user information
data class UserData(
    val name: String,
    val email: String,
    val phone: String
)

