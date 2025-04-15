package com.example.sinoman

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

data class FormData(
    // Personal Data - Page 1
    var name: String = "",
    var nik: String = "",
    var nikPhotoPath: String = "",
    var npwp: String = "",
    var npwpPhotoPath: String = "",
    var familyCardNumber: String = "",
    var familyCardPhotoPath: String = "",
    var currentAddress: String = "",
    var hometownAddress: String = "",
    var gender: String = "",
    var maritalStatus: String = "",
    var numberOfChildren: String = "",
    var education: String = "",
    var occupation: String = "",
    var monthlyIncome: String = "",
    var monthlyAllowance: String = "",
    var monthlyExpenses: String = "",

    // Housing Status - Page 2
    var hasHouse: Boolean? = null,

    // House Owner Data - Page 3a
    var buildingSize: String = "",
    var landSize: String = "",
    var certificateType: String = "",
    var financingInterest: String = "",

    // Non-House Owner Data - Page 3b
    var houseInterest: String = "",
    var locationProvince: String = "",
    var locationCity: String = "",
    var locationDistrict: String = "",
    var locationVillage: String = "",

    // Location data (for house assistance)
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var locationAddress: String = "",

    // Legacy fields (keeping for backward compatibility)
    var address: String = "",
    var phone: String = "",
    var question1Answer: Int = -1,
    var question2Answer: Int = -1,
    var question3Answer: Int = -1
) {
    companion object {
        private const val PREF_NAME = "form_data_prefs"
        private const val KEY_FORM_DATA = "form_data"
        private const val KEY_PERSONAL_DATA_COMPLETED = "personal_data_completed"
        private const val KEY_REGISTRATIONS = "registrations"

        fun save(context: Context, formData: FormData) {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = gson.toJson(formData)
            prefs.edit().putString(KEY_FORM_DATA, json).apply()
        }

        fun load(context: Context): FormData {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = prefs.getString(KEY_FORM_DATA, null)
            return if (json != null) {
                gson.fromJson(json, FormData::class.java)
            } else {
                FormData()
            }
        }

        fun clear(context: Context) {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            prefs.edit().remove(KEY_FORM_DATA).apply()
        }

        fun setPersonalDataCompleted(context: Context, completed: Boolean) {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            prefs.edit().putBoolean(KEY_PERSONAL_DATA_COMPLETED, completed).apply()
        }

        fun isPersonalDataCompleted(context: Context): Boolean {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return prefs.getBoolean(KEY_PERSONAL_DATA_COMPLETED, false)
        }

        fun calculatePersonalDataCompletionPercentage(formData: FormData): Int {
            var filledFields = 0
            var totalFields = 0

            // Page 1 fields
            if (formData.name.isNotBlank()) filledFields++
            if (formData.nik.isNotBlank()) filledFields++
            if (formData.nikPhotoPath.isNotBlank()) filledFields++
            if (formData.npwp.isNotBlank()) filledFields++
            if (formData.npwpPhotoPath.isNotBlank()) filledFields++
            if (formData.familyCardNumber.isNotBlank()) filledFields++
            if (formData.familyCardPhotoPath.isNotBlank()) filledFields++
            if (formData.currentAddress.isNotBlank()) filledFields++
            if (formData.hometownAddress.isNotBlank()) filledFields++
            if (formData.gender.isNotBlank()) filledFields++
            if (formData.maritalStatus.isNotBlank()) filledFields++
            if (formData.numberOfChildren.isNotBlank()) filledFields++
            if (formData.education.isNotBlank()) filledFields++
            if (formData.occupation.isNotBlank()) filledFields++
            if (formData.monthlyIncome.isNotBlank()) filledFields++
            if (formData.monthlyAllowance.isNotBlank()) filledFields++
            if (formData.monthlyExpenses.isNotBlank()) filledFields++
            totalFields += 17

            if (formData.latitude != 0.0 && formData.longitude != 0.0) filledFields++
            if (formData.locationAddress.isNotBlank()) filledFields++
            totalFields += 2

            return if (totalFields > 0) (filledFields * 100) / totalFields else 0
        }

        fun isPersonalDataComplete(formData: FormData): Boolean {
            return formData.name.isNotBlank() &&
                    formData.nik.isNotBlank() &&
                    formData.nikPhotoPath.isNotBlank() &&
                    formData.npwp.isNotBlank() &&
                    formData.npwpPhotoPath.isNotBlank() &&
                    formData.familyCardNumber.isNotBlank() &&
                    formData.familyCardPhotoPath.isNotBlank() &&
                    formData.currentAddress.isNotBlank() &&
                    formData.hometownAddress.isNotBlank() &&
                    formData.gender.isNotBlank() &&
                    formData.maritalStatus.isNotBlank() &&
                    formData.numberOfChildren.isNotBlank() &&
                    formData.education.isNotBlank() &&
                    formData.occupation.isNotBlank() &&
                    formData.monthlyIncome.isNotBlank() &&
                    formData.monthlyAllowance.isNotBlank() &&
                    formData.monthlyExpenses.isNotBlank() &&
                    formData.latitude != 0.0 &&
                    formData.longitude != 0.0 &&
                    formData.locationAddress.isNotBlank()
        }

        fun isAnyFieldFilled(formData: FormData): Boolean {
            return formData.name.isNotBlank() ||
                    formData.nik.isNotBlank() ||
                    formData.nikPhotoPath.isNotBlank() ||
                    formData.npwp.isNotBlank() ||
                    formData.npwpPhotoPath.isNotBlank() ||
                    formData.familyCardNumber.isNotBlank() ||
                    formData.familyCardPhotoPath.isNotBlank() ||
                    formData.currentAddress.isNotBlank() ||
                    formData.hometownAddress.isNotBlank() ||
                    formData.gender.isNotBlank() ||
                    formData.maritalStatus.isNotBlank() ||
                    formData.numberOfChildren.isNotBlank() ||
                    formData.education.isNotBlank() ||
                    formData.occupation.isNotBlank() ||
                    formData.monthlyIncome.isNotBlank() ||
                    formData.monthlyAllowance.isNotBlank() ||
                    formData.monthlyExpenses.isNotBlank()
        }
    }
}

// Registration data class to store information about housing assistance registrations
data class RegistrationData(
    val id: String = UUID.randomUUID().toString(),
    val type: RegistrationType = RegistrationType.HOUSE_OWNER,
    var status: RegistrationStatus = RegistrationStatus.IN_PROGRESS,
    var dateCreated: Long = System.currentTimeMillis(),
    var dateUpdated: Long = System.currentTimeMillis(),

    // House Owner Data - Page 3a
    var buildingSize: String = "",
    var landSize: String = "",
    var certificateType: String = "",
    var financingInterest: String = "",

    // Non-House Owner Data - Page 3b
    var houseInterest: String = "",
    var locationProvince: String = "",
    var locationCity: String = "",
    var locationDistrict: String = "",
    var locationVillage: String = ""
) {
    fun isComplete(): Boolean {
        return when (type) {
            RegistrationType.HOUSE_OWNER -> {
                buildingSize.isNotBlank() &&
                        landSize.isNotBlank() &&
                        certificateType.isNotBlank() &&
                        financingInterest.isNotBlank()
            }
            RegistrationType.NON_HOUSE_OWNER -> {
                houseInterest.isNotBlank() &&
                        locationProvince.isNotBlank() &&
                        locationCity.isNotBlank() &&
                        locationDistrict.isNotBlank() &&
                        locationVillage.isNotBlank()
            }
        }
    }

    fun isAnyFieldFilled(): Boolean {
        return when (type) {
            RegistrationType.HOUSE_OWNER -> {
                buildingSize.isNotBlank() ||
                        landSize.isNotBlank() ||
                        certificateType.isNotBlank() ||
                        financingInterest.isNotBlank()
            }
            RegistrationType.NON_HOUSE_OWNER -> {
                houseInterest.isNotBlank() ||
                        locationProvince.isNotBlank() ||
                        locationCity.isNotBlank() ||
                        locationDistrict.isNotBlank() ||
                        locationVillage.isNotBlank()
            }
        }
    }

    companion object {
        private const val PREF_NAME = "form_data_prefs"
        private const val KEY_REGISTRATIONS = "registrations"

        fun saveRegistrations(context: Context, registrations: List<RegistrationData>) {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = gson.toJson(registrations)
            prefs.edit().putString(KEY_REGISTRATIONS, json).apply()
        }

        fun loadRegistrations(context: Context): List<RegistrationData> {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = prefs.getString(KEY_REGISTRATIONS, null)
            return if (json != null) {
                val type = object : TypeToken<List<RegistrationData>>() {}.type
                gson.fromJson(json, type)
            } else {
                emptyList()
            }
        }

        fun addOrUpdateRegistration(context: Context, registration: RegistrationData) {
            val registrations = loadRegistrations(context).toMutableList()
            val existingIndex = registrations.indexOfFirst { it.id == registration.id }

            if (existingIndex >= 0) {
                // Update existing registration
                registration.dateUpdated = System.currentTimeMillis()
                registrations[existingIndex] = registration
            } else {
                // Add new registration
                registrations.add(registration)
            }

            saveRegistrations(context, registrations)
        }

        fun deleteRegistration(context: Context, registrationId: String) {
            val registrations = loadRegistrations(context).toMutableList()
            registrations.removeIf { it.id == registrationId }
            saveRegistrations(context, registrations)
        }

        fun getRegistration(context: Context, registrationId: String): RegistrationData? {
            val registrations = loadRegistrations(context)
            return registrations.find { it.id == registrationId }
        }

        fun getInProgressRegistration(context: Context, type: RegistrationType): RegistrationData? {
            val registrations = loadRegistrations(context)
            return registrations.find {
                it.type == type &&
                        (it.status == RegistrationStatus.IN_PROGRESS || it.status == RegistrationStatus.UNDER_REVIEW)
            }
        }

        fun hasActiveRegistration(context: Context): Boolean {
            val registrations = loadRegistrations(context)
            return registrations.any {
                it.status == RegistrationStatus.IN_PROGRESS ||
                        it.status == RegistrationStatus.UNDER_REVIEW
            }
        }

        fun hasActiveRegistrationOfType(context: Context, type: RegistrationType): Boolean {
            val registrations = loadRegistrations(context)
            return registrations.any {
                it.type == type &&
                        (it.status == RegistrationStatus.IN_PROGRESS || it.status == RegistrationStatus.UNDER_REVIEW)
            }
        }
    }
}

enum class RegistrationType {
    HOUSE_OWNER,
    NON_HOUSE_OWNER
}

enum class RegistrationStatus {
    IN_PROGRESS,  // Data belum lengkap/belum dikirim
    UNDER_REVIEW, // Data sudah dikirim dan sedang ditinjau
    ACCEPTED,     // Diterima
    REJECTED      // Ditolak
}
