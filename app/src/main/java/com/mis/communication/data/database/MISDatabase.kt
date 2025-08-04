package com.mis.communication.data.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mis.communication.data.database.dao.*
import com.mis.communication.data.database.converters.*
import com.mis.communication.data.model.*
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

/**
 * Merkeziyetsiz MİS veritabanı
 * Şifrelenmiş ve dağıtılmış veri depolama için tasarlanmıştır
 */
@Database(
    entities = [
        User::class,
        Message::class,
        Chat::class,
        P2PNode::class,
        EncryptionKey::class,
        BlockchainTransaction::class,
        MediaFile::class,
        ContactInfo::class,
        CallRecord::class,
        UserStatus::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    ListStringConverter::class,
    MessageTypeConverter::class,
    MessageStatusConverter::class,
    UserStatusConverter::class,
    EncryptionPreferencesConverter::class,
    MessageMetadataConverter::class,
    MessageEditListConverter::class,
    DeliveryReceiptListConverter::class,
    MessageReactionListConverter::class
)
abstract class MISDatabase : RoomDatabase() {

    // DAO interfaces
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao
    abstract fun p2pNodeDao(): P2PNodeDao
    abstract fun encryptionKeyDao(): EncryptionKeyDao
    abstract fun blockchainTransactionDao(): BlockchainTransactionDao
    abstract fun mediaFileDao(): MediaFileDao
    abstract fun contactInfoDao(): ContactInfoDao
    abstract fun callRecordDao(): CallRecordDao
    abstract fun userStatusDao(): UserStatusDao

    companion object {
        @Volatile
        private var INSTANCE: MISDatabase? = null
        private const val DATABASE_NAME = "mis_decentralized_db"
        private const val DATABASE_PASSPHRASE = "MIS_SECURE_2024_DECENTRALIZED"

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildDatabase(context)
                }
            }
        }

        fun getInstance(): MISDatabase {
            return INSTANCE ?: throw IllegalStateException("Database not initialized")
        }

        private fun buildDatabase(context: Context): MISDatabase {
            // SQLCipher encryption support
            val passphrase = SQLiteDatabase.getBytes(DATABASE_PASSPHRASE.toCharArray())
            val factory = SupportFactory(passphrase)

            return Room.databaseBuilder(
                context.applicationContext,
                MISDatabase::class.java,
                DATABASE_NAME
            )
                .openHelperFactory(factory)
                .addMigrations(MIGRATION_1_2)
                .addCallback(databaseCallback)
                .fallbackToDestructiveMigration()
                .build()
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Migration logic for future versions
            }
        }

        private val databaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Initialize with default data if needed
                initializeDefaultData()
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // Enable foreign key constraints
                db.execSQL("PRAGMA foreign_keys=ON")
                // Enable WAL mode for better performance
                db.execSQL("PRAGMA journal_mode=WAL")
                // Set secure delete
                db.execSQL("PRAGMA secure_delete=ON")
            }
        }

        private fun initializeDefaultData() {
            // Initialize with system user and default encryption keys
            val database = getInstance()
            
            // This would typically be done in a background thread
            // For demo purposes, structure is shown here
        }

        /**
         * Cleanup database resources
         */
        fun cleanup() {
            INSTANCE?.close()
            INSTANCE = null
        }

        /**
         * Get database size for analytics
         */
        fun getDatabaseSize(context: Context): Long {
            val dbFile = context.getDatabasePath(DATABASE_NAME)
            return if (dbFile.exists()) dbFile.length() else 0L
        }

        /**
         * Vacuum database to optimize storage
         */
        suspend fun vacuumDatabase() {
            getInstance().clearAllTables()
            getInstance().openHelper.writableDatabase.execSQL("VACUUM")
        }

        /**
         * Export database for backup (encrypted)
         */
        fun exportDatabase(context: Context, destinationPath: String): Boolean {
            return try {
                val currentDbPath = context.getDatabasePath(DATABASE_NAME).absolutePath
                val currentDb = java.io.File(currentDbPath)
                val backupDb = java.io.File(destinationPath)
                
                if (currentDb.exists()) {
                    currentDb.copyTo(backupDb, overwrite = true)
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }
}