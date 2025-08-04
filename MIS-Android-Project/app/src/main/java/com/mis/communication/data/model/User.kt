package com.mis.communication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Merkeziyetsiz kullanıcı modeli
 * Blockchain tabanlı kimlik doğrulama ve P2P ağ için tasarlanmıştır
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @SerializedName("user_id")
    val userId: String = UUID.randomUUID().toString(),
    
    @SerializedName("public_key")
    val publicKey: String, // RSA Public Key for encryption
    
    @SerializedName("wallet_address")
    val walletAddress: String, // Blockchain wallet address
    
    @SerializedName("username")
    val username: String,
    
    @SerializedName("display_name")
    val displayName: String,
    
    @SerializedName("profile_image_hash")
    val profileImageHash: String? = null, // IPFS hash for profile image
    
    @SerializedName("about")
    val about: String = "Hey, I'm using MİS!",
    
    @SerializedName("status")
    val status: UserStatus = UserStatus.OFFLINE,
    
    @SerializedName("last_seen")
    val lastSeen: Long = System.currentTimeMillis(),
    
    @SerializedName("node_id")
    val nodeId: String, // P2P node identifier
    
    @SerializedName("ip_addresses")
    val ipAddresses: List<String> = emptyList(), // Known IP addresses for P2P
    
    @SerializedName("port")
    val port: Int = 0, // P2P communication port
    
    @SerializedName("reputation_score")
    val reputationScore: Double = 0.0, // Blockchain-based reputation
    
    @SerializedName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @SerializedName("updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    
    @SerializedName("is_verified")
    val isVerified: Boolean = false, // Blockchain verification status
    
    @SerializedName("device_fingerprint")
    val deviceFingerprint: String, // Unique device identifier
    
    @SerializedName("supported_protocols")
    val supportedProtocols: List<String> = listOf("MIS-P2P-v1", "WebRTC", "IPFS"),
    
    @SerializedName("encryption_preferences")
    val encryptionPreferences: EncryptionPreferences = EncryptionPreferences()
)

enum class UserStatus {
    ONLINE,
    AWAY,
    BUSY,
    OFFLINE,
    INVISIBLE
}

data class EncryptionPreferences(
    val algorithm: String = "AES-256-GCM",
    val keyExchangeMethod: String = "ECDH-secp256k1",
    val signatureAlgorithm: String = "ECDSA-secp256k1",
    val hashFunction: String = "SHA-256"
)