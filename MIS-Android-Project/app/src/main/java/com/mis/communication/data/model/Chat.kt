package com.mis.communication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Merkeziyetsiz sohbet modeli
 */
@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey
    @SerializedName("chat_id")
    val chatId: String = UUID.randomUUID().toString(),
    
    @SerializedName("chat_type")
    val chatType: ChatType = ChatType.DIRECT,
    
    @SerializedName("title")
    val title: String? = null, // For group chats
    
    @SerializedName("participants")
    val participants: List<String> = emptyList(), // User IDs
    
    @SerializedName("admins")
    val admins: List<String> = emptyList(), // Admin user IDs for groups
    
    @SerializedName("creator_id")
    val creatorId: String,
    
    @SerializedName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @SerializedName("updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    
    @SerializedName("last_message_id")
    val lastMessageId: String? = null,
    
    @SerializedName("last_message_timestamp")
    val lastMessageTimestamp: Long = 0,
    
    @SerializedName("unread_count")
    val unreadCount: Int = 0,
    
    @SerializedName("is_muted")
    val isMuted: Boolean = false,
    
    @SerializedName("is_archived")
    val isArchived: Boolean = false,
    
    @SerializedName("is_pinned")
    val isPinned: Boolean = false,
    
    @SerializedName("encryption_key_id")
    val encryptionKeyId: String,
    
    @SerializedName("blockchain_contract_address")
    val blockchainContractAddress: String? = null, // Smart contract for group governance
    
    @SerializedName("distributed_nodes")
    val distributedNodes: List<String> = emptyList(), // P2P nodes storing this chat
    
    @SerializedName("chat_settings")
    val chatSettings: ChatSettings = ChatSettings()
)

enum class ChatType {
    DIRECT,
    GROUP,
    BROADCAST,
    CHANNEL,
    SECRET // Encrypted group with advanced security
}

data class ChatSettings(
    val disappearingMessagesEnabled: Boolean = false,
    val disappearingMessagesDuration: Long = 0, // in milliseconds
    val allowAddMembers: Boolean = true,
    val allowEditInfo: Boolean = true,
    val allowSendMessages: Boolean = true,
    val allowSendMedia: Boolean = true,
    val onlyAdminsCanAdd: Boolean = false,
    val onlyAdminsCanEdit: Boolean = false,
    val autoDeleteOldMessages: Boolean = false,
    val maxMessageHistory: Int = 10000
)