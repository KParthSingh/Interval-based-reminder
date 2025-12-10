package com.medicinereminder.app

import android.content.Context

class SettingsRepository(context: Context) {
    private val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    
    companion object {
        const val KEY_THEME = "theme_mode"
        const val KEY_CLOSE_ON_START = "close_on_start"
        
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
        const val THEME_AUTO = "auto"
    }
    
    fun getThemeMode(): String {
        return prefs.getString(KEY_THEME, THEME_AUTO) ?: THEME_AUTO
    }
    
    fun setThemeMode(mode: String) {
        prefs.edit().putString(KEY_THEME, mode).apply()
    }
    
    fun getCloseOnStart(): Boolean {
        return prefs.getBoolean(KEY_CLOSE_ON_START, false)
    }
    
    fun setCloseOnStart(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_CLOSE_ON_START, enabled).apply()
    }
}
