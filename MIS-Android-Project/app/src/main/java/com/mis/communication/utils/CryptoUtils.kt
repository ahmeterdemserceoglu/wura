package com.mis.communication.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.google.crypto.tink.Aead
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.JsonKeysetReader
import com.google.crypto.tink.JsonKeysetWriter
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.signature.PublicKeySignConfig
import com.google.crypto.tink.signature.PublicKeyVerifyConfig
import com.mis.communication.data.model.Message
import org.bouncycastle.crypto.generators.ECKeyPairGenerator
import org.bouncycastle.crypto.params.ECDomainParameters
import org.bouncycastle.crypto.params.ECKeyGenerationParameters
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.cert.X509Certificate
import java.security.spec.ECGenParameterSpec
import java.util.*
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Gelişmiş şifreleme ve güvenlik araçları
 * Signal Protocol, Google Tink ve BouncyCastle kullanır
 */
object CryptoUtils {
    
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val AES_KEY_SIZE = 256
    private const val RSA_KEY_SIZE = 4096
    private const val EC_CURVE = "secp256k1"
    private const val GCM_IV_LENGTH = 12
    private const val GCM_TAG_LENGTH = 16
    
    init {
        // Initialize BouncyCastle provider
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(BouncyCastleProvider())
        }
        
        // Initialize Google Tink
        AeadConfig.register()
        PublicKeySignConfig.register()
        PublicKeyVerifyConfig.register()
    }

    /**
     * Generate elliptic curve key pair for blockchain operations
     */
    fun generateECKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME)
        val ecGenParameterSpec = ECGenParameterSpec(EC_CURVE)
        keyPairGenerator.initialize(ecGenParameterSpec, SecureRandom())
        return keyPairGenerator.generateKeyPair()
    }

    /**
     * Generate RSA key pair for secure communication
     */
    fun generateRSAKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(RSA_KEY_SIZE, SecureRandom())
        return keyPairGenerator.generateKeyPair()
    }

    /**
     * Generate AES key using Android Keystore
     */
    fun generateAESKey(alias: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(AES_KEY_SIZE)
            .setUserAuthenticationRequired(false)
            .build()
        
        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    /**
     * Encrypt message using Google Tink AEAD
     */
    fun encryptMessage(message: Message): String {
        try {
            val keysetHandle = KeysetHandle.generateNew(AeadKeyTemplates.AES256_GCM)
            val aead = keysetHandle.getPrimitive(Aead::class.java)
            
            val plaintext = MessageSerializer.serialize(message).toByteArray(StandardCharsets.UTF_8)
            val associatedData = message.messageId.toByteArray(StandardCharsets.UTF_8)
            
            val ciphertext = aead.encrypt(plaintext, associatedData)
            
            // Store keyset securely (implementation needed)
            storeKeyset(keysetHandle, message.encryptionKeyId)
            
            return Base64.getEncoder().encodeToString(ciphertext)
        } catch (e: Exception) {
            throw SecurityException("Message encryption failed", e)
        }
    }

    /**
     * Decrypt message using Google Tink AEAD
     */
    fun decryptMessage(encryptedMessage: String, keyId: String, messageId: String): Message {
        try {
            val keysetHandle = loadKeyset(keyId)
            val aead = keysetHandle.getPrimitive(Aead::class.java)
            
            val ciphertext = Base64.getDecoder().decode(encryptedMessage)
            val associatedData = messageId.toByteArray(StandardCharsets.UTF_8)
            
            val decryptedBytes = aead.decrypt(ciphertext, associatedData)
            val decryptedJson = String(decryptedBytes, StandardCharsets.UTF_8)
            
            return MessageSerializer.deserialize(decryptedJson)
        } catch (e: Exception) {
            throw SecurityException("Message decryption failed", e)
        }
    }

    /**
     * Generate digital signature for message authenticity
     */
    fun signMessage(message: String, privateKey: PrivateKey): String {
        try {
            val signature = Signature.getInstance("SHA256withECDSA", BouncyCastleProvider.PROVIDER_NAME)
            signature.initSign(privateKey)
            signature.update(message.toByteArray(StandardCharsets.UTF_8))
            
            val signatureBytes = signature.sign()
            return Base64.getEncoder().encodeToString(signatureBytes)
        } catch (e: Exception) {
            throw SecurityException("Message signing failed", e)
        }
    }

    /**
     * Verify digital signature
     */
    fun verifySignature(message: String, signatureString: String, publicKey: PublicKey): Boolean {
        return try {
            val signature = Signature.getInstance("SHA256withECDSA", BouncyCastleProvider.PROVIDER_NAME)
            signature.initVerify(publicKey)
            signature.update(message.toByteArray(StandardCharsets.UTF_8))
            
            val signatureBytes = Base64.getDecoder().decode(signatureString)
            signature.verify(signatureBytes)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Generate secure hash using SHA-3
     */
    fun generateSecureHash(data: String): String {
        try {
            val digest = MessageDigest.getInstance("SHA3-256", BouncyCastleProvider.PROVIDER_NAME)
            val hashBytes = digest.digest(data.toByteArray(StandardCharsets.UTF_8))
            return Base64.getEncoder().encodeToString(hashBytes)
        } catch (e: Exception) {
            throw SecurityException("Hash generation failed", e)
        }
    }

    /**
     * Generate HMAC for message integrity
     */
    fun generateHMAC(data: String, key: SecretKey): String {
        try {
            val mac = Mac.getInstance("HmacSHA256")
            mac.init(key)
            val hmacBytes = mac.doFinal(data.toByteArray(StandardCharsets.UTF_8))
            return Base64.getEncoder().encodeToString(hmacBytes)
        } catch (e: Exception) {
            throw SecurityException("HMAC generation failed", e)
        }
    }

    /**
     * Verify HMAC
     */
    fun verifyHMAC(data: String, hmacString: String, key: SecretKey): Boolean {
        return try {
            val expectedHMAC = generateHMAC(data, key)
            MessageDigest.isEqual(
                expectedHMAC.toByteArray(StandardCharsets.UTF_8),
                hmacString.toByteArray(StandardCharsets.UTF_8)
            )
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Generate wallet address from public key (for blockchain integration)
     */
    fun generateWalletAddress(publicKey: PublicKey): String {
        try {
            val publicKeyBytes = publicKey.encoded
            val hash = MessageDigest.getInstance("SHA256").digest(publicKeyBytes)
            val ripemd = MessageDigest.getInstance("RIPEMD160", BouncyCastleProvider.PROVIDER_NAME)
            val addressHash = ripemd.digest(hash)
            
            // Add version byte (0x00 for main network)
            val versionedPayload = byteArrayOf(0x00) + addressHash
            
            // Calculate checksum
            val checksum = MessageDigest.getInstance("SHA256")
                .digest(MessageDigest.getInstance("SHA256").digest(versionedPayload))
                .take(4).toByteArray()
            
            // Create full address
            val fullAddress = versionedPayload + checksum
            
            return Base58.encode(fullAddress)
        } catch (e: Exception) {
            throw SecurityException("Wallet address generation failed", e)
        }
    }

    /**
     * Encrypt file for secure storage
     */
    fun encryptFile(inputFile: File, outputFile: File, key: SecretKey) {
        try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            
            val iv = cipher.iv
            
            inputFile.inputStream().use { input ->
                outputFile.outputStream().use { output ->
                    // Write IV first
                    output.write(iv)
                    
                    // Encrypt and write data
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        val encryptedBytes = cipher.update(buffer, 0, bytesRead)
                        if (encryptedBytes != null) {
                            output.write(encryptedBytes)
                        }
                    }
                    
                    // Write final block
                    val finalBytes = cipher.doFinal()
                    if (finalBytes != null) {
                        output.write(finalBytes)
                    }
                }
            }
        } catch (e: Exception) {
            throw SecurityException("File encryption failed", e)
        }
    }

    /**
     * Decrypt file
     */
    fun decryptFile(inputFile: File, outputFile: File, key: SecretKey) {
        try {
            inputFile.inputStream().use { input ->
                // Read IV
                val iv = ByteArray(GCM_IV_LENGTH)
                input.read(iv)
                
                val cipher = Cipher.getInstance("AES/GCM/NoPadding")
                val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
                cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec)
                
                outputFile.outputStream().use { output ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        val decryptedBytes = cipher.update(buffer, 0, bytesRead)
                        if (decryptedBytes != null) {
                            output.write(decryptedBytes)
                        }
                    }
                    
                    // Write final block
                    val finalBytes = cipher.doFinal()
                    if (finalBytes != null) {
                        output.write(finalBytes)
                    }
                }
            }
        } catch (e: Exception) {
            throw SecurityException("File decryption failed", e)
        }
    }

    /**
     * Generate random salt for password hashing
     */
    fun generateSalt(): ByteArray {
        val salt = ByteArray(32)
        SecureRandom().nextBytes(salt)
        return salt
    }

    /**
     * Generate secure random bytes
     */
    fun generateSecureRandomBytes(length: Int): ByteArray {
        val bytes = ByteArray(length)
        SecureRandom().nextBytes(bytes)
        return bytes
    }

    // Helper methods for server certificates (mock implementation)
    fun getServerCertificate(): X509Certificate {
        // In real implementation, load from secure storage
        throw NotImplementedError("Server certificate loading not implemented")
    }

    fun getServerPrivateKey(): PrivateKey {
        // In real implementation, load from secure storage
        throw NotImplementedError("Server private key loading not implemented")
    }

    // Private helper methods
    private fun storeKeyset(keysetHandle: KeysetHandle, keyId: String) {
        // Implement secure keyset storage
        // Could use encrypted SharedPreferences or Android Keystore
    }

    private fun loadKeyset(keyId: String): KeysetHandle {
        // Implement keyset loading
        // Return stored keyset for the given keyId
        throw NotImplementedError("Keyset loading not implemented")
    }

    // Mock Base58 implementation (use a proper library in production)
    private object Base58 {
        private const val ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
        
        fun encode(input: ByteArray): String {
            // Simplified Base58 encoding (use proper library in production)
            return Base64.getEncoder().encodeToString(input)
        }
    }
}