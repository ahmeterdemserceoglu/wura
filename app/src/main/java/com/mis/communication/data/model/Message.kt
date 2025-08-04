package com.mis.communication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Merkeziyetsiz mesaj modeli
 * Blockchain doğrulama ve P2P dağıtımı için tasarlanmıştır
 */
@Entity(tableName = "messages")
data class Message(
    @PrimaryKey
    @SerializedName("message_id")
    val messageId: String = UUID.randomUUID().toString(),
    
    @SerializedName("chat_id")
    val chatId: String,
    
    @SerializedName("sender_id")
    val senderId: String,
    
    @SerializedName("sender_wallet_address")
    val senderWalletAddress: String, // Blockchain verification
    
    @SerializedName("content")
    val content: String, // Encrypted content
    
    @SerializedName("content_type")
    val contentType: MessageType = MessageType.TEXT,
    
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis(),
    
    @SerializedName("blockchain_hash")
    val blockchainHash: String? = null, // Hash on blockchain for verification
    
    @SerializedName("ipfs_hash")
    val ipfsHash: String? = null, // IPFS storage hash for media
    
    @SerializedName("signature")
    val signature: String, // Digital signature for authenticity
    
    @SerializedName("encryption_key_id")
    val encryptionKeyId: String, // Reference to encryption key
    
    @SerializedName("message_status")
    val messageStatus: MessageStatus = MessageStatus.SENDING,
    
    @SerializedName("reply_to_message_id")
    val replyToMessageId: String? = null,
    
    @SerializedName("forward_from_message_id")
    val forwardFromMessageId: String? = null,
    
    @SerializedName("edit_history")
    val editHistory: List<MessageEdit> = emptyList(),
    
    @SerializedName("delivery_receipts")
    val deliveryReceipts: List<DeliveryReceipt> = emptyList(),
    
    @SerializedName("reactions")
    val reactions: List<MessageReaction> = emptyList(),
    
    @SerializedName("expires_at")
    val expiresAt: Long? = null, // For disappearing messages
    
    @SerializedName("priority")
    val priority: MessagePriority = MessagePriority.NORMAL,
    
    @SerializedName("metadata")
    val metadata: MessageMetadata? = null,
    
    @SerializedName("distributed_nodes")
    val distributedNodes: List<String> = emptyList(), // Nodes that have this message
    
    @SerializedName("verification_status")
    val verificationStatus: VerificationStatus = VerificationStatus.PENDING
)

enum class MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    AUDIO,
    DOCUMENT,
    LOCATION,
    CONTACT,
    STICKER,
    GIF,
    VOICE_NOTE,
    VIDEO_NOTE,
    POLL,
    PAYMENT, // Cryptocurrency payment
    SMART_CONTRACT, // Smart contract interaction
    NFT, // NFT transfer
    SYSTEM
}

enum class MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ,
    FAILED,
    DELETED
}

enum class MessagePriority {
    LOW,
    NORMAL,
    HIGH,
    URGENT
}

enum class VerificationStatus {
    PENDING,
    VERIFIED,
    FAILED,
    REVOKED
}

data class MessageEdit(
    val editId: String = UUID.randomUUID().toString(),
    val previousContent: String,
    val newContent: String,
    val editedAt: Long = System.currentTimeMillis(),
    val editedByUserId: String
)

data class DeliveryReceipt(
    val userId: String,
    val nodeId: String,
    val deliveredAt: Long = System.currentTimeMillis(),
    val readAt: Long? = null,
    val signature: String // Cryptographic proof of delivery
)

data class MessageReaction(
    val reactionId: String = UUID.randomUUID().toString(),
    val userId: String,
    val emoji: String,
    val reactedAt: Long = System.currentTimeMillis(),
    val signature: String
)

data class MessageMetadata(
    val fileSize: Long? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val duration: Long? = null, // For audio/video
    val width: Int? = null, // For images/videos
    val height: Int? = null,
    val thumbnailHash: String? = null, // IPFS hash for thumbnail
    val latitude: Double? = null, // For location messages
    val longitude: Double? = null,
    val contactName: String? = null, // For contact messages
    val contactPhone: String? = null
)