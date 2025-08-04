package com.mis.communication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * P2P ağ düğümü modeli
 * Merkeziyetsiz ağdaki her bir düğümü temsil eder
 */
@Entity(tableName = "p2p_nodes")
data class P2PNode(
    @PrimaryKey
    @SerializedName("node_id")
    val nodeId: String = UUID.randomUUID().toString(),
    
    @SerializedName("user_id")
    val userId: String? = null, // Associated user (if any)
    
    @SerializedName("public_key")
    val publicKey: String,
    
    @SerializedName("ip_address")
    val ipAddress: String,
    
    @SerializedName("port")
    val port: Int,
    
    @SerializedName("protocol_version")
    val protocolVersion: String = "MIS-P2P-v1.0",
    
    @SerializedName("supported_features")
    val supportedFeatures: List<String> = emptyList(),
    
    @SerializedName("reputation_score")
    val reputationScore: Double = 0.0,
    
    @SerializedName("last_seen")
    val lastSeen: Long = System.currentTimeMillis(),
    
    @SerializedName("connection_status")
    val connectionStatus: NodeConnectionStatus = NodeConnectionStatus.DISCONNECTED,
    
    @SerializedName("latency")
    val latency: Long = 0, // in milliseconds
    
    @SerializedName("bandwidth")
    val bandwidth: Long = 0, // in bytes per second
    
    @SerializedName("storage_capacity")
    val storageCapacity: Long = 0, // in bytes
    
    @SerializedName("available_storage")
    val availableStorage: Long = 0, // in bytes
    
    @SerializedName("geo_location")
    val geoLocation: GeoLocation? = null,
    
    @SerializedName("is_trusted")
    val isTrusted: Boolean = false,
    
    @SerializedName("trust_level")
    val trustLevel: TrustLevel = TrustLevel.UNKNOWN,
    
    @SerializedName("connection_attempts")
    val connectionAttempts: Int = 0,
    
    @SerializedName("successful_connections")
    val successfulConnections: Int = 0,
    
    @SerializedName("data_shared")
    val dataShared: Long = 0, // bytes shared with this node
    
    @SerializedName("data_received")
    val dataReceived: Long = 0, // bytes received from this node
    
    @SerializedName("blockchain_verified")
    val blockchainVerified: Boolean = false,
    
    @SerializedName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @SerializedName("updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

enum class NodeConnectionStatus {
    CONNECTED,
    CONNECTING,
    DISCONNECTED,
    FAILED,
    BANNED
}

enum class TrustLevel {
    UNKNOWN,
    LOW,
    MEDIUM,
    HIGH,
    VERIFIED
}

data class GeoLocation(
    val country: String,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val isp: String? = null
)