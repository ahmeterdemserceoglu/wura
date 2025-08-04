package com.mis.communication.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Uygulama tercihlerini güvenli bir şekilde yöneten sınıf
 */
class PreferenceManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "mis_preferences"
        
        // Keys
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_USER_PHOTO = "user_photo"
        private const val KEY_USER_ABOUT = "user_about"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_VIBRATION_ENABLED = "vibration_enabled"
        private const val KEY_BACKUP_ENABLED = "backup_enabled"
        private const val KEY_ENCRYPTION_ENABLED = "encryption_enabled"
        private const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
        private const val KEY_AUTO_DOWNLOAD_WIFI = "auto_download_wifi"
        private const val KEY_AUTO_DOWNLOAD_MOBILE = "auto_download_mobile"
        private const val KEY_CHAT_WALLPAPER = "chat_wallpaper"
        private const val KEY_FONT_SIZE = "font_size"
        private const val KEY_LAST_BACKUP_TIME = "last_backup_time"
    }

    private val sharedPreferences: SharedPreferences

    init {
        // Güvenli SharedPreferences kullan
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // User Information
    fun setUserId(userId: String) = putString(KEY_USER_ID, userId)
    fun getUserId(): String = getString(KEY_USER_ID, "")

    fun setUserName(name: String) = putString(KEY_USER_NAME, name)
    fun getUserName(): String = getString(KEY_USER_NAME, "")

    fun setUserPhone(phone: String) = putString(KEY_USER_PHONE, phone)
    fun getUserPhone(): String = getString(KEY_USER_PHONE, "")

    fun setUserPhoto(photoUrl: String) = putString(KEY_USER_PHOTO, photoUrl)
    fun getUserPhoto(): String = getString(KEY_USER_PHOTO, "")

    fun setUserAbout(about: String) = putString(KEY_USER_ABOUT, about)
    fun getUserAbout(): String = getString(KEY_USER_ABOUT, "Hey, MİS kullanıyorum!")

    // Authentication
    fun setLoggedIn(isLoggedIn: Boolean) = putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
    fun isLoggedIn(): Boolean = getBoolean(KEY_IS_LOGGED_IN, false)

    // Theme and Appearance
    fun setThemeMode(themeMode: Int) = putInt(KEY_THEME_MODE, themeMode)
    fun getThemeMode(): Int = getInt(KEY_THEME_MODE, 0) // 0: System, 1: Light, 2: Dark

    fun setLanguage(language: String) = putString(KEY_LANGUAGE, language)
    fun getLanguage(): String = getString(KEY_LANGUAGE, "tr")

    fun setChatWallpaper(wallpaperPath: String) = putString(KEY_CHAT_WALLPAPER, wallpaperPath)
    fun getChatWallpaper(): String = getString(KEY_CHAT_WALLPAPER, "")

    fun setFontSize(fontSize: Int) = putInt(KEY_FONT_SIZE, fontSize)
    fun getFontSize(): Int = getInt(KEY_FONT_SIZE, 14)

    // Notifications
    fun setNotificationsEnabled(enabled: Boolean) = putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled)
    fun isNotificationsEnabled(): Boolean = getBoolean(KEY_NOTIFICATIONS_ENABLED, true)

    fun setSoundEnabled(enabled: Boolean) = putBoolean(KEY_SOUND_ENABLED, enabled)
    fun isSoundEnabled(): Boolean = getBoolean(KEY_SOUND_ENABLED, true)

    fun setVibrationEnabled(enabled: Boolean) = putBoolean(KEY_VIBRATION_ENABLED, enabled)
    fun isVibrationEnabled(): Boolean = getBoolean(KEY_VIBRATION_ENABLED, true)

    // Security and Privacy
    fun setEncryptionEnabled(enabled: Boolean) = putBoolean(KEY_ENCRYPTION_ENABLED, enabled)
    fun isEncryptionEnabled(): Boolean = getBoolean(KEY_ENCRYPTION_ENABLED, true)

    fun setBiometricEnabled(enabled: Boolean) = putBoolean(KEY_BIOMETRIC_ENABLED, enabled)
    fun isBiometricEnabled(): Boolean = getBoolean(KEY_BIOMETRIC_ENABLED, false)

    // Backup
    fun setBackupEnabled(enabled: Boolean) = putBoolean(KEY_BACKUP_ENABLED, enabled)
    fun isBackupEnabled(): Boolean = getBoolean(KEY_BACKUP_ENABLED, true)

    fun setLastBackupTime(time: Long) = putLong(KEY_LAST_BACKUP_TIME, time)
    fun getLastBackupTime(): Long = getLong(KEY_LAST_BACKUP_TIME, 0L)

    // Auto Download
    fun setAutoDownloadWifi(enabled: Boolean) = putBoolean(KEY_AUTO_DOWNLOAD_WIFI, enabled)
    fun isAutoDownloadWifiEnabled(): Boolean = getBoolean(KEY_AUTO_DOWNLOAD_WIFI, true)

    fun setAutoDownloadMobile(enabled: Boolean) = putBoolean(KEY_AUTO_DOWNLOAD_MOBILE, enabled)
    fun isAutoDownloadMobileEnabled(): Boolean = getBoolean(KEY_AUTO_DOWNLOAD_MOBILE, false)

    // Helper methods
    private fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    private fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    private fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    private fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    private fun putLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    private fun getLong(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    /**
     * Tüm kullanıcı verilerini temizler (çıkış işlemi için)
     */
    fun clearUserData() {
        sharedPreferences.edit().apply {
            remove(KEY_USER_ID)
            remove(KEY_USER_NAME)
            remove(KEY_USER_PHONE)
            remove(KEY_USER_PHOTO)
            remove(KEY_USER_ABOUT)
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }
    }

    /**
     * Tüm tercihleri temizler
     */
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}