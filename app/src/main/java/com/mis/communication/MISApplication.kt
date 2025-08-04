package com.mis.communication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.Configuration
import androidx.work.WorkManager
import com.mis.communication.constants.NotificationConstants
import com.mis.communication.data.database.MISDatabase
import com.mis.communication.utils.CrashHandler
import com.mis.communication.utils.ThemeManager

/**
 * MİS (Mobil İletişim Sistemi) - Main Application Class
 * WhatsApp'tan daha gelişmiş özellikler sunan mobil iletişim uygulaması
 */
class MISApplication : Application() {

    companion object {
        lateinit var instance: MISApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Crash handler'ı başlat
        CrashHandler.initialize(this)
        
        // Tema yöneticisini başlat
        ThemeManager.initialize(this)
        
        // Bildirim kanallarını oluştur
        createNotificationChannels()
        
        // Veritabanını başlat
        MISDatabase.initialize(this)
        
        // WorkManager'ı yapılandır
        setupWorkManager()
        
        // Firebase'i başlat
        initializeFirebase()
    }

    /**
     * Bildirim kanallarını oluşturur
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            // Mesaj bildirimleri kanalı
            val messageChannel = NotificationChannel(
                NotificationConstants.CHANNEL_MESSAGES,
                getString(R.string.notification_channel_messages),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Yeni mesajlar için bildirimler"
                enableVibration(true)
                enableLights(true)
                setShowBadge(true)
            }
            
            // Arama bildirimleri kanalı
            val callChannel = NotificationChannel(
                NotificationConstants.CHANNEL_CALLS,
                getString(R.string.notification_channel_calls),
                NotificationManager.IMPORTANCE_MAX
            ).apply {
                description = "Gelen aramalar için bildirimler"
                enableVibration(true)
                enableLights(true)
                setShowBadge(true)
            }
            
            // Durum güncellemeleri kanalı
            val statusChannel = NotificationChannel(
                NotificationConstants.CHANNEL_STATUS,
                "Durum Güncellemeleri",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Durum güncellemeleri için bildirimler"
                enableVibration(false)
                enableLights(true)
                setShowBadge(false)
            }
            
            // Sistem bildirimleri kanalı
            val systemChannel = NotificationChannel(
                NotificationConstants.CHANNEL_SYSTEM,
                "Sistem",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Sistem bildirimleri"
                enableVibration(false)
                enableLights(false)
                setShowBadge(false)
            }
            
            notificationManager.createNotificationChannels(
                listOf(messageChannel, callChannel, statusChannel, systemChannel)
            )
        }
    }

    /**
     * WorkManager'ı yapılandırır
     */
    private fun setupWorkManager() {
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(if (BuildConfig.DEBUG) android.util.Log.DEBUG else android.util.Log.INFO)
            .build()
        
        WorkManager.initialize(this, config)
    }

    /**
     * Firebase servislerini başlatır
     */
    private fun initializeFirebase() {
        // Firebase Analytics
        // Firebase Crashlytics
        // Firebase Messaging
        // Burada Firebase konfigürasyonları yapılacak
    }

    /**
     * Uygulama hafızasının düşük olduğu durumlarda çağrılır
     */
    override fun onLowMemory() {
        super.onLowMemory()
        // Cache temizleme işlemleri
        clearCaches()
    }

    /**
     * Uygulama cache'lerini temizler
     */
    private fun clearCaches() {
        // Image cache temizleme
        // Database cache temizleme
        // Network cache temizleme
    }

    /**
     * Uygulama context'ini döndürür
     */
    fun getAppContext(): Context = applicationContext
}