package com.appinsnap.aishrm.util

/**
 * Created by Adnan Bashir manak on 19,May,2023
 * AIS company,
 * Krachi, Pakistan.
 */
class SessionManager {
    private var lastActivityTime: Long = 0
    private val sessionTimeout: Long =5 * 60 * 1000 // 5 minutes in milliseconds
    fun startSession() {
        updateActivityTime()
    }

    fun updateSession() {
        updateActivityTime()
    }

    fun isSessionExpired(): Boolean {
        val elapsedTime = System.currentTimeMillis() - lastActivityTime
        return elapsedTime > sessionTimeout
    }

    private fun updateActivityTime() {
        lastActivityTime = System.currentTimeMillis()
    }
}