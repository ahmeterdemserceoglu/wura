package com.mis.communication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Blockchain transaction modeli
 * Mesaj doğrulama ve mikro ödemeler için
 */
@Entity(tableName = "blockchain_transactions")
data class BlockchainTransaction(
    @PrimaryKey
    @SerializedName("transaction_id")
    val transactionId: String = UUID.randomUUID().toString(),
    
    @SerializedName("blockchain_hash")
    val blockchainHash: String, // Actual blockchain transaction hash
    
    @SerializedName("transaction_type")
    val transactionType: TransactionType = TransactionType.MESSAGE_VERIFICATION,
    
    @SerializedName("from_address")
    val fromAddress: String,
    
    @SerializedName("to_address")
    val toAddress: String? = null,
    
    @SerializedName("amount")
    val amount: Double = 0.0, // For payment transactions
    
    @SerializedName("currency")
    val currency: String = "MIS", // MIS token or other cryptocurrency
    
    @SerializedName("gas_fee")
    val gasFee: Double = 0.0,
    
    @SerializedName("data_payload")
    val dataPayload: String? = null, // Additional data (message hash, etc.)
    
    @SerializedName("message_id")
    val messageId: String? = null, // Associated message if any
    
    @SerializedName("chat_id")
    val chatId: String? = null, // Associated chat if any
    
    @SerializedName("block_number")
    val blockNumber: Long? = null,
    
    @SerializedName("block_hash")
    val blockHash: String? = null,
    
    @SerializedName("confirmation_count")
    val confirmationCount: Int = 0,
    
    @SerializedName("status")
    val status: TransactionStatus = TransactionStatus.PENDING,
    
    @SerializedName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @SerializedName("confirmed_at")
    val confirmedAt: Long? = null,
    
    @SerializedName("network")
    val network: String = "MIS-Chain", // Blockchain network name
    
    @SerializedName("smart_contract_address")
    val smartContractAddress: String? = null,
    
    @SerializedName("nonce")
    val nonce: Long = 0,
    
    @SerializedName("signature")
    val signature: String, // Transaction signature
    
    @SerializedName("public_key")
    val publicKey: String, // Sender's public key
    
    @SerializedName("metadata")
    val metadata: TransactionMetadata? = null
)

enum class TransactionType {
    MESSAGE_VERIFICATION,
    PAYMENT,
    FILE_STORAGE,
    IDENTITY_VERIFICATION,
    SMART_CONTRACT_CALL,
    TOKEN_TRANSFER,
    GROUP_MANAGEMENT,
    KEY_EXCHANGE
}

enum class TransactionStatus {
    PENDING,
    CONFIRMED,
    FAILED,
    REJECTED,
    CANCELLED
}

data class TransactionMetadata(
    val messageHash: String? = null,
    val fileHash: String? = null,
    val recipientCount: Int? = null,
    val encryptionKeyId: String? = null,
    val priority: Int = 1,
    val expiresAt: Long? = null
)