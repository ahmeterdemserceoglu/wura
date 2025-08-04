package com.mis.communication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Kişi bilgileri modeli
 */
@Entity(tableName = "contacts")
data class ContactInfo(
    @PrimaryKey
    @SerializedName("contact_id")
    val contactId: String = UUID.randomUUID().toString(),
    
    @SerializedName("user_id")
    val userId: String? = null, // MİS kullanıcısı ise
    
    @SerializedName("display_name")
    val displayName: String,
    
    @SerializedName("phone_number")
    val phoneNumber: String,
    
    @SerializedName("email")
    val email: String? = null,
    
    @SerializedName("profile_image_hash")
    val profileImageHash: String? = null, // IPFS hash
    
    @SerializedName("is_mis_user")
    val isMisUser: Boolean = false,
    
    @SerializedName("public_key")
    val publicKey: String? = null, // For MİS users
    
    @SerializedName("wallet_address")
    val walletAddress: String? = null, // For MİS users
    
    @SerializedName("last_seen")
    val lastSeen: Long? = null,
    
    @SerializedName("status")
    val status: com.mis.communication.data.model.UserStatus = com.mis.communication.data.model.UserStatus.OFFLINE,
    
    @SerializedName("about")
    val about: String? = null,
    
    @SerializedName("is_blocked")
    val isBlocked: Boolean = false,
    
    @SerializedName("is_favorite")
    val isFavorite: Boolean = false,
    
    @SerializedName("added_at")
    val addedAt: Long = System.currentTimeMillis(),
    
    @SerializedName("updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    
    @SerializedName("contact_source")
    val contactSource: ContactSource = ContactSource.MANUAL,
    
    @SerializedName("verification_status")
    val verificationStatus: ContactVerificationStatus = ContactVerificationStatus.UNVERIFIED,
    
    @SerializedName("trust_level")
    val trustLevel: TrustLevel = TrustLevel.UNKNOWN,
    
    @SerializedName("shared_groups")
    val sharedGroups: List<String> = emptyList(), // Group IDs
    
    @SerializedName("interaction_count")
    val interactionCount: Int = 0,
    
    @SerializedName("last_interaction")
    val lastInteraction: Long? = null,
    
    @SerializedName("custom_ringtone")
    val customRingtone: String? = null,
    
    @SerializedName("notification_settings")
    val notificationSettings: ContactNotificationSettings = ContactNotificationSettings()
)

enum class ContactSource {
    MANUAL,
    PHONE_BOOK,
    QR_CODE,
    INVITATION,
    GROUP_MEMBER,
    NEARBY_DISCOVERY
}

enum class ContactVerificationStatus {
    UNVERIFIED,
    PHONE_VERIFIED,
    BLOCKCHAIN_VERIFIED,
    MUTUALLY_VERIFIED
}

data class ContactNotificationSettings(
    val enabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val customSound: String? = null
)