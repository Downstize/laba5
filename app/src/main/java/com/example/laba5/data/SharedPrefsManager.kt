package com.example.laba5.data

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    fun saveFontSize(size: Float) {
        prefs.edit().putFloat("font_size", size).apply()
    }

    fun saveLanguage(language: String) {
        prefs.edit().putString("language", language).apply()
    }

    fun getLanguage(): String {
        return prefs.getString("language", "en") ?: "en"
    }

    fun getFontSize(): Float {
        return prefs.getFloat("font_size", 16f)
    }
}