package com.mis.communication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Arama kayıtları modeli
 */
@Entity(tableName = "call_records")
data class CallRecord(
    @PrimaryKey
    @SerializedName("call_id")
    val callId: String = UUID.randomUUID().toString(),
    
    @SerializedName("call_type")
    val callType: CallType = CallType.VOICE,
    
    @SerializedName("direction")
    val direction: CallDirection = CallDirection.OUTGOING,
    
    @SerializedName("caller_id")
    val callerId: String,
    
    @SerializedName("callee_id")
    val calleeId: String,
    
    @SerializedName("participants")
    val participants: List<String> = emptyList(), // For group calls
    
    @SerializedName("start_time")
    val startTime: Long = System.currentTimeMillis(),
    
    @SerializedName("end_time")
    val endTime: Long? = null,
    
    @SerializedName("duration")
    val duration: Long = 0, // in milliseconds
    
    @SerializedName("status")
    val status: CallStatus = CallStatus.INITIATED,
    
    @SerializedName("quality_rating")
    val qualityRating: Int? = null, // 1-5 stars
    
    @SerializedName("network_type")
    val networkType: String? = null, // WiFi, 4G, 5G, etc.
    
    @SerializedName("encryption_enabled")
    val encryptionEnabled: Boolean = true,
    
    @SerializedName("recording_enabled")
    val recordingEnabled: Boolean = false,
    
    @SerializedName("recording_path")
    val recordingPath: String? = null,
    
    @SerializedName("p2p_connection")
    val p2pConnection: Boolean = false, // Direct P2P or through relay
    
    @SerializedName("relay_nodes")
    val relayNodes: List<String> = emptyList(), // If using relay
    
    @SerializedName("call_metadata")
    val callMetadata: CallMetadata? = null,
    
    @SerializedName("blockchain_hash")
    val blockchainHash: String? = null, // For call verification
    
    @SerializedName("is_emergency")
    val isEmergency: Boolean = false,
    
    @SerializedName("cost")
    val cost: Double = 0.0, // If using paid calling
    
    @SerializedName("currency")
    val currency: String = "MIS"
)

enum class CallType {
    VOICE,
    VIDEO,
    SCREEN_SHARE,
    GROUP_VOICE,
    GROUP_VIDEO,
    CONFERENCE
}

enum class CallDirection {
    INCOMING,
    OUTGOING,
    MISSED
}

enum class CallStatus {
    INITIATED,
    RINGING,
    ANSWERED,
    ENDED,
    MISSED,
    REJECTED,
    BUSY,
    FAILED,
    CANCELLED
}

data class CallMetadata(
    val audioCodec: String? = null,
    val videoCodec: String? = null,
    val resolution: String? = null, // For video calls
    val frameRate: Int? = null,
    val bitrate: Int? = null,
    val packetLoss: Float? = null,
    val latency: Int? = null, // in milliseconds
    val jitter: Int? = null,
    val bandwidthUsed: Long? = null, // in bytes
    val serverRegion: String? = null
)