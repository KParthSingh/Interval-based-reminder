package com.medicinereminder.app

import android.content.Context

class ChainManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("chain_prefs", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_CHAIN_ACTIVE = "chain_active"
        private const val KEY_CURRENT_INDEX = "current_index"
    }
    
    fun startChain() {
        prefs.edit()
            .putBoolean(KEY_CHAIN_ACTIVE, true)
            .putInt(KEY_CURRENT_INDEX, 0)
            .apply()
    }
    
    fun isChainActive(): Boolean {
        return prefs.getBoolean(KEY_CHAIN_ACTIVE, false)
    }
    
    fun getCurrentIndex(): Int {
        return prefs.getInt(KEY_CURRENT_INDEX, 0)
    }
    
    fun moveToNextAlarm() {
        val currentIndex = getCurrentIndex()
        prefs.edit()
            .putInt(KEY_CURRENT_INDEX, currentIndex + 1)
            .apply()
    }
    
    fun stopChain() {
        prefs.edit()
            .putBoolean(KEY_CHAIN_ACTIVE, false)
            .putInt(KEY_CURRENT_INDEX, 0)
            .apply()
    }
}
