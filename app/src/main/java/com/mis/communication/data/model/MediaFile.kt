package com.mis.communication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Medya dosyaları modeli
 * IPFS ve merkeziyetsiz depolama için
 */
@Entity(tableName = "media_files")
data class MediaFile(
    @PrimaryKey
    @SerializedName("file_id")
    val fileId: String = UUID.randomUUID().toString(),
    
    @SerializedName("original_name")
    val originalName: String,
    
    @SerializedName("file_type")
    val fileType: MediaType = MediaType.IMAGE,
    
    @SerializedName("mime_type")
    val mimeType: String,
    
    @SerializedName("file_size")
    val fileSize: Long,
    
    @SerializedName("local_path")
    val localPath: String? = null, // Local file path if cached
    
    @SerializedName("ipfs_hash")
    val ipfsHash: String? = null, // IPFS content hash
    
    @SerializedName("encryption_key_id")
    val encryptionKeyId: String? = null, // If file is encrypted
    
    @SerializedName("thumbnail_hash")
    val thumbnailHash: String? = null, // IPFS hash for thumbnail
    
    @SerializedName("width")
    val width: Int? = null, // For images/videos
    
    @SerializedName("height")
    val height: Int? = null, // For images/videos
    
    @SerializedName("duration")
    val duration: Long? = null, // For audio/video in milliseconds
    
    @SerializedName("bitrate")
    val bitrate: Int? = null, // For audio/video
    
    @SerializedName("message_id")
    val messageId: String? = null, // Associated message
    
    @SerializedName("uploader_user_id")
    val uploaderUserId: String,
    
    @SerializedName("upload_status")
    val uploadStatus: UploadStatus = UploadStatus.PENDING,
    
    @SerializedName("download_status")
    val downloadStatus: DownloadStatus = DownloadStatus.NOT_DOWNLOADED,
    
    @SerializedName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @SerializedName("uploaded_at")
    val uploadedAt: Long? = null,
    
    @SerializedName("expires_at")
    val expiresAt: Long? = null, // For temporary files
    
    @SerializedName("access_count")
    val accessCount: Int = 0,
    
    @SerializedName("last_accessed")
    val lastAccessed: Long? = null,
    
    @SerializedName("compression_info")
    val compressionInfo: CompressionInfo? = null,
    
    @SerializedName("metadata")
    val metadata: MediaMetadata? = null,
    
    @SerializedName("distributed_nodes")
    val distributedNodes: List<String> = emptyList(), // Nodes storing this file
    
    @SerializedName("blockchain_hash")
    val blockchainHash: String? = null // Blockchain verification hash
)

enum class MediaType {
    IMAGE,
    VIDEO,
    AUDIO,
    DOCUMENT,
    ARCHIVE,
    OTHER
}

enum class UploadStatus {
    PENDING,
    UPLOADING,
    UPLOADED,
    FAILED,
    CANCELLED
}

enum class DownloadStatus {
    NOT_DOWNLOADED,
    DOWNLOADING,
    DOWNLOADED,
    FAILED,
    EXPIRED
}

data class CompressionInfo(
    val originalSize: Long,
    val compressedSize: Long,
    val compressionRatio: Float,
    val algorithm: String = "gzip"
)

data class MediaMetadata(
    val title: String? = null,
    val description: String? = null,
    val tags: List<String> = emptyList(),
    val location: Location? = null,
    val cameraInfo: CameraInfo? = null,
    val editHistory: List<EditInfo> = emptyList()
)

data class Location(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double? = null,
    val accuracy: Float? = null
)

data class CameraInfo(
    val make: String? = null,
    val model: String? = null,
    val iso: Int? = null,
    val exposureTime: String? = null,
    val fNumber: Float? = null,
    val focalLength: Float? = null
)

data class EditInfo(
    val editId: String = UUID.randomUUID().toString(),
    val editType: String, // crop, resize, filter, etc.
    val editedAt: Long = System.currentTimeMillis(),
    val editedBy: String? = null
)