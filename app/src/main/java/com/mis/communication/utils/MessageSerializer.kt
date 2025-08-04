package com.mis.communication.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.mis.communication.data.model.*
import java.lang.reflect.Type

/**
 * Mesaj serileştirme ve deserileştirme işlemleri
 */
object MessageSerializer {
    
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(MessageType::class.java, MessageTypeAdapter())
        .registerTypeAdapter(MessageStatus::class.java, MessageStatusAdapter())
        .registerTypeAdapter(com.mis.communication.data.model.UserStatus::class.java, UserStatusAdapter())
        .registerTypeAdapter(VerificationStatus::class.java, VerificationStatusAdapter())
        .registerTypeAdapter(MessagePriority::class.java, MessagePriorityAdapter())
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .create()

    /**
     * Mesajı JSON string'e serialize eder
     */
    fun serialize(message: Message): String {
        return gson.toJson(message)
    }

    /**
     * JSON string'i Message objesine deserialize eder
     */
    fun deserialize(json: String): Message {
        return gson.fromJson(json, Message::class.java)
    }

    /**
     * Kullanıcıyı JSON string'e serialize eder
     */
    fun serialize(user: User): String {
        return gson.toJson(user)
    }

    /**
     * JSON string'i User objesine deserialize eder
     */
    fun deserializeUser(json: String): User {
        return gson.fromJson(json, User::class.java)
    }

    /**
     * P2P Node'u JSON string'e serialize eder
     */
    fun serialize(node: P2PNode): String {
        return gson.toJson(node)
    }

    /**
     * JSON string'i P2PNode objesine deserialize eder
     */
    fun deserializeNode(json: String): P2PNode {
        return gson.fromJson(json, P2PNode::class.java)
    }

    /**
     * Sohbeti JSON string'e serialize eder
     */
    fun serialize(chat: Chat): String {
        return gson.toJson(chat)
    }

    /**
     * JSON string'i Chat objesine deserialize eder
     */
    fun deserializeChat(json: String): Chat {
        return gson.fromJson(json, Chat::class.java)
    }

    // Type Adapters for enums
    private class MessageTypeAdapter : JsonSerializer<MessageType>, JsonDeserializer<MessageType> {
        override fun serialize(src: MessageType?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(src?.name)
        }

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): MessageType {
            return try {
                MessageType.valueOf(json?.asString ?: "TEXT")
            } catch (e: IllegalArgumentException) {
                MessageType.TEXT
            }
        }
    }

    private class MessageStatusAdapter : JsonSerializer<MessageStatus>, JsonDeserializer<MessageStatus> {
        override fun serialize(src: MessageStatus?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(src?.name)
        }

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): MessageStatus {
            return try {
                MessageStatus.valueOf(json?.asString ?: "SENDING")
            } catch (e: IllegalArgumentException) {
                MessageStatus.SENDING
            }
        }
    }

    private class UserStatusAdapter : JsonSerializer<com.mis.communication.data.model.UserStatus>, JsonDeserializer<com.mis.communication.data.model.UserStatus> {
        override fun serialize(src: com.mis.communication.data.model.UserStatus?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(src?.name)
        }

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): com.mis.communication.data.model.UserStatus {
            return try {
                com.mis.communication.data.model.UserStatus.valueOf(json?.asString ?: "OFFLINE")
            } catch (e: IllegalArgumentException) {
                com.mis.communication.data.model.UserStatus.OFFLINE
            }
        }
    }

    private class VerificationStatusAdapter : JsonSerializer<VerificationStatus>, JsonDeserializer<VerificationStatus> {
        override fun serialize(src: VerificationStatus?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(src?.name)
        }

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): VerificationStatus {
            return try {
                VerificationStatus.valueOf(json?.asString ?: "PENDING")
            } catch (e: IllegalArgumentException) {
                VerificationStatus.PENDING
            }
        }
    }

    private class MessagePriorityAdapter : JsonSerializer<MessagePriority>, JsonDeserializer<MessagePriority> {
        override fun serialize(src: MessagePriority?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(src?.name)
        }

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): MessagePriority {
            return try {
                MessagePriority.valueOf(json?.asString ?: "NORMAL")
            } catch (e: IllegalArgumentException) {
                MessagePriority.NORMAL
            }
        }
    }
}