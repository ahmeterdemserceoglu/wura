package com.mis.communication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Şifreleme anahtarları modeli
 * Merkeziyetsiz sistemde anahtar yönetimi için
 */
@Entity(tableName = "encryption_keys")
data class EncryptionKey(
    @PrimaryKey
    @SerializedName("key_id")
    val keyId: String = UUID.randomUUID().toString(),
    
    @SerializedName("key_type")
    val keyType: KeyType = KeyType.AES,
    
    @SerializedName("algorithm")
    val algorithm: String = "AES-256-GCM",
    
    @SerializedName("key_data")
    val keyData: String, // Base64 encoded key
    
    @SerializedName("public_key")
    val publicKey: String? = null, // For asymmetric keys
    
    @SerializedName("private_key")
    val privateKey: String? = null, // For asymmetric keys (encrypted)
    
    @SerializedName("owner_user_id")
    val ownerUserId: String,
    
    @SerializedName("chat_id")
    val chatId: String? = null, // Associated chat if any
    
    @SerializedName("key_purpose")
    val keyPurpose: KeyPurpose = KeyPurpose.MESSAGE_ENCRYPTION,
    
    @SerializedName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @SerializedName("expires_at")
    val expiresAt: Long? = null,
    
    @SerializedName("is_active")
    val isActive: Boolean = true,
    
    @SerializedName("key_version")
    val keyVersion: Int = 1,
    
    @SerializedName("rotation_count")
    val rotationCount: Int = 0,
    
    @SerializedName("shared_with")
    val sharedWith: List<String> = emptyList(), // User IDs who have access
    
    @SerializedName("blockchain_hash")
    val blockchainHash: String? = null, // Hash stored on blockchain
    
    @SerializedName("key_derivation_info")
    val keyDerivationInfo: KeyDerivationInfo? = null
)

enum class KeyType {
    AES,
    RSA,
    ECDSA,
    ECDH,
    HMAC,
    PBKDF2
}

enum class KeyPurpose {
    MESSAGE_ENCRYPTION,
    FILE_ENCRYPTION,
    SIGNATURE,
    KEY_EXCHANGE,
    AUTHENTICATION,
    BLOCKCHAIN_WALLET
}

data class KeyDerivationInfo(
    val salt: String, // Base64 encoded
    val iterations: Int = 100000,
    val keyLength: Int = 256,
    val hashFunction: String = "SHA-256"
)