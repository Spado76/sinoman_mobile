package com.example.sinoman

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

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

        fun calculateCompletionPercentage(formData: FormData): Int {
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

            // Page 2 and 3 fields
            if (formData.hasHouse != null) {
                filledFields++
                totalFields++

                if (formData.hasHouse == true) {
                    // Page 3a fields
                    if (formData.buildingSize.isNotBlank()) filledFields++
                    if (formData.landSize.isNotBlank()) filledFields++
                    if (formData.certificateType.isNotBlank()) filledFields++
                    if (formData.financingInterest.isNotBlank()) filledFields++
                    totalFields += 4
                } else {
                    // Page 3b fields
                    if (formData.houseInterest.isNotBlank()) filledFields++
                    if (formData.locationProvince.isNotBlank()) filledFields++
                    if (formData.locationCity.isNotBlank()) filledFields++
                    if (formData.locationDistrict.isNotBlank()) filledFields++
                    if (formData.locationVillage.isNotBlank()) filledFields++
                    totalFields += 5
                }
            }

            return if (totalFields > 0) (filledFields * 100) / totalFields else 0
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
                    formData.monthlyExpenses.isNotBlank() ||
                    formData.hasHouse != null
        }
    }
}

