package com.mis.communication.ai

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mis.communication.data.model.*
import com.mis.communication.utils.CryptoUtils
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.*
import kotlin.random.Random

/**
 * MİS Akıllı Motor - AI Powered Features
 * 
 * Özellikler:
 * - Akıllı mesaj yönlendirme
 * - Spam algılama
 * - Kullanıcı davranış analizi
 * - Otomatik çeviri
 * - Akıllı bildirim yönetimi
 * - Predictive caching
 * - Network optimization
 */
class MISIntelligenceEngine private constructor(
    private val context: Context
) {
    companion object {
        @Volatile
        private var INSTANCE: MISIntelligenceEngine? = null
        
        fun getInstance(context: Context): MISIntelligenceEngine {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MISIntelligenceEngine(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    // AI State Management
    private val aiScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val userBehaviorMap = ConcurrentHashMap<String, UserBehaviorProfile>()
    private val networkOptimizationCache = ConcurrentHashMap<String, NetworkRoute>()
    private val spamDetectionModel = SpamDetectionModel()
    private val translationCache = ConcurrentHashMap<String, String>()
    
    // Live Data for AI insights
    private val _aiInsights = MutableLiveData<AIInsight>()
    val aiInsights: LiveData<AIInsight> = _aiInsights
    
    private val _networkOptimization = MutableLiveData<NetworkOptimization>()
    val networkOptimization: LiveData<NetworkOptimization> = _networkOptimization

    init {
        startAIServices()
    }

    /**
     * Akıllı mesaj yönlendirme algoritması
     * Mesh network'te en optimal yolu bulur
     */
    suspend fun findOptimalRoute(
        targetUserId: String,
        availableNodes: List<P2PNode>,
        messageSize: Long,
        priority: MessagePriority
    ): List<P2PNode> = withContext(Dispatchers.Default) {
        
        // AI-powered routing algorithm
        val routes = mutableListOf<NetworkRoute>()
        
        // 1. Direct connection check
        val directNode = availableNodes.find { it.userId == targetUserId }
        if (directNode != null && isNodeReliable(directNode)) {
            return@withContext listOf(directNode)
        }
        
        // 2. Multi-hop routing with AI optimization
        val visitedNodes = mutableSetOf<String>()
        val routeQueue = mutableListOf<RouteCandidate>()
        
        // Initialize with first-hop nodes
        availableNodes.forEach { node ->
            if (node.userId != targetUserId && isNodeReliable(node)) {
                routeQueue.add(
                    RouteCandidate(
                        nodes = listOf(node),
                        totalLatency = node.latency,
                        totalCost = calculateRouteCost(node, messageSize),
                        reliability = calculateReliability(node)
                    )
                )
            }
        }
        
        // AI-enhanced pathfinding
        while (routeQueue.isNotEmpty()) {
            // Sort by AI score (latency + cost + reliability + behavioral patterns)
            routeQueue.sortBy { candidate ->
                calculateAIRouteScore(candidate, targetUserId, priority)
            }
            
            val bestCandidate = routeQueue.removeAt(0)
            val lastNode = bestCandidate.nodes.last()
            
            if (lastNode.userId == targetUserId) {
                // Found target!
                cacheRoute(targetUserId, bestCandidate.nodes)
                return@withContext bestCandidate.nodes
            }
            
            if (bestCandidate.nodes.size >= 5) continue // Max hop limit
            
            // Expand to neighboring nodes
            getNeighboringNodes(lastNode).forEach { neighbor ->
                if (!visitedNodes.contains(neighbor.nodeId) && 
                    !bestCandidate.nodes.any { it.nodeId == neighbor.nodeId }) {
                    
                    routeQueue.add(
                        RouteCandidate(
                            nodes = bestCandidate.nodes + neighbor,
                            totalLatency = bestCandidate.totalLatency + neighbor.latency,
                            totalCost = bestCandidate.totalCost + calculateRouteCost(neighbor, messageSize),
                            reliability = min(bestCandidate.reliability, calculateReliability(neighbor))
                        )
                    )
                }
            }
            
            visitedNodes.add(lastNode.nodeId)
        }
        
        // Fallback: return best available nodes
        return@withContext availableNodes.take(3)
    }

    /**
     * AI tabanlı spam algılama
     */
    fun analyzeMessageForSpam(message: Message, senderProfile: UserBehaviorProfile?): SpamAnalysisResult {
        val features = extractSpamFeatures(message, senderProfile)
        val spamScore = spamDetectionModel.predict(features)
        
        return SpamAnalysisResult(
            isSpam = spamScore > 0.7,
            confidence = spamScore,
            reasons = generateSpamReasons(features, spamScore),
            recommendedAction = when {
                spamScore > 0.9 -> SpamAction.BLOCK
                spamScore > 0.7 -> SpamAction.QUARANTINE
                spamScore > 0.4 -> SpamAction.WARNING
                else -> SpamAction.ALLOW
            }
        )
    }

    /**
     * Kullanıcı davranış profili oluşturma
     */
    fun updateUserBehaviorProfile(userId: String, action: UserAction) {
        val profile = userBehaviorMap.getOrPut(userId) { UserBehaviorProfile(userId) }
        
        aiScope.launch {
            profile.apply {
                totalActions++
                lastActivity = System.currentTimeMillis()
                
                when (action.type) {
                    ActionType.MESSAGE_SENT -> {
                        messagesSent++
                        avgMessageLength = ((avgMessageLength * (messagesSent - 1)) + action.messageLength) / messagesSent
                    }
                    ActionType.MESSAGE_READ -> messagesRead++
                    ActionType.CALL_MADE -> callsMade++
                    ActionType.FILE_SHARED -> filesShared++
                    ActionType.GROUP_CREATED -> groupsCreated++
                }
                
                // Update behavioral patterns
                updateBehavioralPatterns(action)
                
                // Calculate trust score
                trustScore = calculateTrustScore()
                
                // Update reputation
                reputationScore = calculateReputationScore()
            }
            
            userBehaviorMap[userId] = profile
            
            // Generate AI insights
            generateUserInsights(profile)
        }
    }

    /**
     * Akıllı çeviri sistemi
     */
    suspend fun translateMessage(
        text: String,
        fromLanguage: String,
        toLanguage: String
    ): TranslationResult = withContext(Dispatchers.IO) {
        
        val cacheKey = "$text|$fromLanguage|$toLanguage"
        translationCache[cacheKey]?.let { cachedTranslation ->
            return@withContext TranslationResult(
                translatedText = cachedTranslation,
                confidence = 1.0,
                fromCache = true
            )
        }
        
        // AI-powered translation (mock implementation)
        val translatedText = performAITranslation(text, fromLanguage, toLanguage)
        val confidence = calculateTranslationConfidence(text, translatedText)
        
        // Cache successful translations
        if (confidence > 0.8) {
            translationCache[cacheKey] = translatedText
        }
        
        TranslationResult(
            translatedText = translatedText,
            confidence = confidence,
            fromCache = false
        )
    }

    /**
     * Akıllı bildirim yönetimi
     */
    fun optimizeNotification(message: Message, userContext: UserContext): NotificationStrategy {
        val profile = userBehaviorMap[message.senderId]
        val currentTime = System.currentTimeMillis()
        
        return when {
            // Acil mesajlar
            message.priority == MessagePriority.URGENT -> NotificationStrategy.IMMEDIATE
            
            // Kullanıcı aktif değil
            !userContext.isUserActive -> {
                when {
                    isImportantSender(message.senderId, profile) -> NotificationStrategy.IMMEDIATE
                    isWorkingHours(currentTime) -> NotificationStrategy.DELAYED
                    else -> NotificationStrategy.SILENT
                }
            }
            
            // Kullanıcı başka bir sohbette aktif
            userContext.activeInOtherChat -> NotificationStrategy.SUBTLE
            
            // Normal durum
            else -> NotificationStrategy.NORMAL
        }
    }

    /**
     * Predictive caching - Hangi mesajların/dosyaların cache'leneceğini öngörür
     */
    fun predictCachingNeeds(userId: String): List<CachingRecommendation> {
        val profile = userBehaviorMap[userId] ?: return emptyList()
        val recommendations = mutableListOf<CachingRecommendation>()
        
        // Frequently contacted users' media
        profile.frequentContacts.forEach { contactId ->
            recommendations.add(
                CachingRecommendation(
                    type = CacheType.USER_MEDIA,
                    targetId = contactId,
                    priority = CachePriority.HIGH,
                    reason = "Frequently contacted user"
                )
            )
        }
        
        // Predict based on time patterns
        val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        profile.hourlyActivityPattern[currentHour]?.let { activity ->
            if (activity > 0.5) {
                recommendations.add(
                    CachingRecommendation(
                        type = CacheType.RECENT_MESSAGES,
                        targetId = userId,
                        priority = CachePriority.MEDIUM,
                        reason = "High activity predicted for this hour"
                    )
                )
            }
        }
        
        return recommendations
    }

    // Private helper methods
    private fun startAIServices() {
        aiScope.launch {
            // Background AI services
            while (true) {
                try {
                    // Network optimization analysis
                    analyzeNetworkPatterns()
                    
                    // User behavior analysis
                    analyzeBehaviorPatterns()
                    
                    // Cache optimization
                    optimizeCaching()
                    
                    delay(60000) // Run every minute
                } catch (e: Exception) {
                    // Handle AI service errors gracefully
                    delay(5000)
                }
            }
        }
    }

    private fun calculateAIRouteScore(
        candidate: RouteCandidate,
        targetUserId: String,
        priority: MessagePriority
    ): Double {
        val latencyWeight = when (priority) {
            MessagePriority.URGENT -> 0.5
            MessagePriority.HIGH -> 0.3
            MessagePriority.NORMAL -> 0.2
            MessagePriority.LOW -> 0.1
        }
        
        val costWeight = 0.2
        val reliabilityWeight = 0.3
        
        return (candidate.totalLatency * latencyWeight) +
               (candidate.totalCost * costWeight) +
               ((1.0 - candidate.reliability) * reliabilityWeight)
    }

    private fun isNodeReliable(node: P2PNode): Boolean {
        return node.reputationScore > 0.7 && 
               node.connectionStatus == NodeConnectionStatus.CONNECTED &&
               node.trustLevel in listOf(TrustLevel.HIGH, TrustLevel.VERIFIED)
    }

    private fun calculateRouteCost(node: P2PNode, messageSize: Long): Double {
        // Cost based on node capacity, bandwidth, and historical performance
        val bandwidthCost = messageSize.toDouble() / (node.bandwidth + 1)
        val reputationCost = 1.0 - node.reputationScore
        return bandwidthCost + reputationCost
    }

    private fun calculateReliability(node: P2PNode): Double {
        val successRate = if (node.connectionAttempts > 0) {
            node.successfulConnections.toDouble() / node.connectionAttempts
        } else 0.5
        
        return (successRate + node.reputationScore + node.trustLevel.ordinal * 0.2) / 3.0
    }

    private fun extractSpamFeatures(message: Message, profile: UserBehaviorProfile?): SpamFeatures {
        return SpamFeatures(
            messageLength = message.content.length,
            hasUrls = message.content.contains("http"),
            hasPhoneNumbers = Regex("\\b\\d{10,}\\b").containsMatchIn(message.content),
            repeatedChars = calculateRepeatedChars(message.content),
            capsRatio = message.content.count { it.isUpperCase() }.toFloat() / message.content.length,
            senderReputation = profile?.reputationScore ?: 0.5,
            senderTrust = profile?.trustScore ?: 0.5,
            timeOfDay = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY),
            messagingFrequency = profile?.getRecentMessagingFrequency() ?: 0.0
        )
    }

    private suspend fun analyzeNetworkPatterns() {
        // AI analysis of network performance patterns
        val optimization = NetworkOptimization(
            recommendedNodes = findOptimalNodes(),
            bandwidthPrediction = predictBandwidthUsage(),
            maintenanceWindow = findOptimalMaintenanceWindow()
        )
        
        _networkOptimization.postValue(optimization)
    }

    private suspend fun analyzeBehaviorPatterns() {
        userBehaviorMap.values.forEach { profile ->
            val insights = generateBehaviorInsights(profile)
            _aiInsights.postValue(insights)
        }
    }

    private fun performAITranslation(text: String, from: String, to: String): String {
        // Mock AI translation - in real implementation, use ML models
        return "[$to] $text"
    }

    private fun calculateTranslationConfidence(original: String, translated: String): Double {
        // Mock confidence calculation
        return Random.nextDouble(0.7, 0.95)
    }

    // Data classes for AI features
    data class RouteCandidate(
        val nodes: List<P2PNode>,
        val totalLatency: Long,
        val totalCost: Double,
        val reliability: Double
    )

    data class SpamFeatures(
        val messageLength: Int,
        val hasUrls: Boolean,
        val hasPhoneNumbers: Boolean,
        val repeatedChars: Float,
        val capsRatio: Float,
        val senderReputation: Double,
        val senderTrust: Double,
        val timeOfDay: Int,
        val messagingFrequency: Double
    )

    data class UserBehaviorProfile(
        val userId: String,
        var totalActions: Int = 0,
        var messagesSent: Int = 0,
        var messagesRead: Int = 0,
        var callsMade: Int = 0,
        var filesShared: Int = 0,
        var groupsCreated: Int = 0,
        var avgMessageLength: Double = 0.0,
        var lastActivity: Long = System.currentTimeMillis(),
        var trustScore: Double = 0.5,
        var reputationScore: Double = 0.5,
        val frequentContacts: MutableSet<String> = mutableSetOf(),
        val hourlyActivityPattern: MutableMap<Int, Double> = mutableMapOf(),
        val behavioralTags: MutableSet<String> = mutableSetOf()
    ) {
        fun getRecentMessagingFrequency(): Double {
            val hoursAgo24 = System.currentTimeMillis() - (24 * 60 * 60 * 1000)
            return if (lastActivity > hoursAgo24) messagesSent / 24.0 else 0.0
        }
        
        fun updateBehavioralPatterns(action: UserAction) {
            val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
            hourlyActivityPattern[hour] = (hourlyActivityPattern[hour] ?: 0.0) + 0.1
        }
        
        fun calculateTrustScore(): Double {
            // AI-based trust calculation
            return min(1.0, (totalActions * 0.001) + (reputationScore * 0.5) + 0.3)
        }
        
        fun calculateReputationScore(): Double {
            // Community-based reputation
            return min(1.0, trustScore * 0.8 + 0.2)
        }
    }

    // Enums and data classes
    enum class ActionType {
        MESSAGE_SENT, MESSAGE_READ, CALL_MADE, FILE_SHARED, GROUP_CREATED
    }

    enum class SpamAction {
        ALLOW, WARNING, QUARANTINE, BLOCK
    }

    enum class NotificationStrategy {
        IMMEDIATE, DELAYED, SUBTLE, SILENT, NORMAL
    }

    enum class CacheType {
        USER_MEDIA, RECENT_MESSAGES, FREQUENT_CONTACTS, PREDICTED_CONTENT
    }

    enum class CachePriority {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    data class UserAction(
        val type: ActionType,
        val timestamp: Long = System.currentTimeMillis(),
        val messageLength: Int = 0,
        val targetUserId: String? = null
    )

    data class SpamAnalysisResult(
        val isSpam: Boolean,
        val confidence: Double,
        val reasons: List<String>,
        val recommendedAction: SpamAction
    )

    data class TranslationResult(
        val translatedText: String,
        val confidence: Double,
        val fromCache: Boolean
    )

    data class UserContext(
        val isUserActive: Boolean,
        val activeInOtherChat: Boolean,
        val currentLocation: String? = null,
        val batteryLevel: Int = 100,
        val networkType: String = "WiFi"
    )

    data class CachingRecommendation(
        val type: CacheType,
        val targetId: String,
        val priority: CachePriority,
        val reason: String
    )

    data class AIInsight(
        val type: String,
        val title: String,
        val description: String,
        val actionable: Boolean = false,
        val action: String? = null
    )

    data class NetworkOptimization(
        val recommendedNodes: List<P2PNode>,
        val bandwidthPrediction: Map<String, Double>,
        val maintenanceWindow: Pair<Long, Long>?
    )

    // Mock implementations for complex AI functions
    private fun calculateRepeatedChars(text: String): Float = 0.1f
    private fun generateSpamReasons(features: SpamFeatures, score: Double): List<String> = emptyList()
    private fun getNeighboringNodes(node: P2PNode): List<P2PNode> = emptyList()
    private fun cacheRoute(targetUserId: String, nodes: List<P2PNode>) {}
    private fun isImportantSender(senderId: String, profile: UserBehaviorProfile?): Boolean = false
    private fun isWorkingHours(timestamp: Long): Boolean = true
    private fun findOptimalNodes(): List<P2PNode> = emptyList()
    private fun predictBandwidthUsage(): Map<String, Double> = emptyMap()
    private fun findOptimalMaintenanceWindow(): Pair<Long, Long>? = null
    private fun generateBehaviorInsights(profile: UserBehaviorProfile): AIInsight = 
        AIInsight("behavior", "User Pattern", "Normal activity detected")
    private fun generateUserInsights(profile: UserBehaviorProfile) {}
    private fun optimizeCaching() {}

    // Simple spam detection model
    private class SpamDetectionModel {
        fun predict(features: SpamFeatures): Double {
            var score = 0.0
            
            if (features.hasUrls) score += 0.3
            if (features.hasPhoneNumbers) score += 0.2
            if (features.capsRatio > 0.5) score += 0.2
            if (features.repeatedChars > 0.3) score += 0.1
            if (features.senderReputation < 0.3) score += 0.4
            if (features.messagingFrequency > 10) score += 0.2
            
            return min(1.0, score)
        }
    }

    data class NetworkRoute(
        val targetUserId: String,
        val nodes: List<P2PNode>,
        val lastUsed: Long = System.currentTimeMillis(),
        val successRate: Double = 1.0
    )
}