package com.mis.communication.features.gamification

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mis.communication.ai.MISIntelligenceEngine
import com.mis.communication.data.model.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.*

/**
 * MİS Gamification Engine
 * 
 * Özellikler:
 * - Achievement System (Başarı Sistemi)
 * - Daily Challenges (Günlük Görevler)
 * - Social Rewards (Sosyal Ödüller)
 * - Privacy Points (Gizlilik Puanları)
 * - Community Contributions (Topluluk Katkıları)
 * - Leaderboards (Liderlik Tabloları)
 * - Virtual Economy (Sanal Ekonomi)
 */
class MISGamificationEngine private constructor(
    private val context: Context
) {
    companion object {
        @Volatile
        private var INSTANCE: MISGamificationEngine? = null
        
        fun getInstance(context: Context): MISGamificationEngine {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MISGamificationEngine(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val gamificationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    // Live Data for gamification events
    private val _achievements = MutableLiveData<List<Achievement>>()
    val achievements: LiveData<List<Achievement>> = _achievements
    
    private val _dailyChallenges = MutableLiveData<List<DailyChallenge>>()
    val dailyChallenges: LiveData<List<DailyChallenge>> = _dailyChallenges
    
    private val _userStats = MutableLiveData<UserGameStats>()
    val userStats: LiveData<UserGameStats> = _userStats
    
    private val _leaderboard = MutableLiveData<List<LeaderboardEntry>>()
    val leaderboard: LiveData<List<LeaderboardEntry>> = _leaderboard

    // Internal state
    private val userAchievements = mutableMapOf<String, MutableSet<AchievementType>>()
    private val userStats = mutableMapOf<String, UserGameStats>()
    private val dailyChallenges = mutableListOf<DailyChallenge>()

    init {
        initializeGamification()
    }

    /**
     * Kullanıcı aksiyonu gerçekleştiğinde çağrılır
     */
    fun onUserAction(userId: String, action: GameAction) {
        gamificationScope.launch {
            val stats = getUserStats(userId)
            
            // Update statistics
            updateUserStats(stats, action)
            
            // Check for achievements
            checkAchievements(userId, stats, action)
            
            // Update daily challenges
            updateDailyChallenges(userId, action)
            
            // Calculate rewards
            val rewards = calculateRewards(action, stats)
            if (rewards.isNotEmpty()) {
                grantRewards(userId, rewards)
            }
            
            // Update leaderboards
            updateLeaderboards(userId, stats)
        }
    }

    /**
     * Günlük görevleri oluşturur
     */
    private fun generateDailyChallenges(userId: String): List<DailyChallenge> {
        val challenges = mutableListOf<DailyChallenge>()
        val random = Random()
        
        // Communication challenges
        challenges.add(
            DailyChallenge(
                id = "daily_messages_${Date().time}",
                type = ChallengeType.SEND_MESSAGES,
                title = "Günlük Sohbet",
                description = "5 farklı kişiye mesaj gönder",
                targetValue = 5,
                currentProgress = 0,
                reward = Reward(RewardType.MIS_COINS, 50),
                expiresAt = getEndOfDay()
            )
        )
        
        // Privacy challenges
        challenges.add(
            DailyChallenge(
                id = "privacy_check_${Date().time}",
                type = ChallengeType.PRIVACY_ACTIONS,
                title = "Gizlilik Uzmanı",
                description = "Gizlilik ayarlarını kontrol et",
                targetValue = 1,
                currentProgress = 0,
                reward = Reward(RewardType.PRIVACY_POINTS, 100),
                expiresAt = getEndOfDay()
            )
        )
        
        // Network contribution challenges
        challenges.add(
            DailyChallenge(
                id = "network_help_${Date().time}",
                type = ChallengeType.NETWORK_CONTRIBUTION,
                title = "Ağ Destekçisi",
                description = "2 saat boyunca P2P node olarak çalış",
                targetValue = 2 * 60, // minutes
                currentProgress = 0,
                reward = Reward(RewardType.REPUTATION_BOOST, 25),
                expiresAt = getEndOfDay()
            )
        )
        
        return challenges
    }

    /**
     * Başarıları kontrol eder
     */
    private suspend fun checkAchievements(userId: String, stats: UserGameStats, action: GameAction) {
        val newAchievements = mutableListOf<Achievement>()
        val userAchievementSet = userAchievements.getOrPut(userId) { mutableSetOf() }
        
        // Communication achievements
        if (stats.messagesSent >= 100 && !userAchievementSet.contains(AchievementType.CHATTER)) {
            newAchievements.add(createAchievement(AchievementType.CHATTER))
            userAchievementSet.add(AchievementType.CHATTER)
        }
        
        if (stats.messagesSent >= 1000 && !userAchievementSet.contains(AchievementType.SOCIAL_BUTTERFLY)) {
            newAchievements.add(createAchievement(AchievementType.SOCIAL_BUTTERFLY))
            userAchievementSet.add(AchievementType.SOCIAL_BUTTERFLY)
        }
        
        // Privacy achievements
        if (stats.privacyActionsCount >= 10 && !userAchievementSet.contains(AchievementType.PRIVACY_ADVOCATE)) {
            newAchievements.add(createAchievement(AchievementType.PRIVACY_ADVOCATE))
            userAchievementSet.add(AchievementType.PRIVACY_ADVOCATE)
        }
        
        // Network contribution achievements
        if (stats.networkContributionHours >= 24 && !userAchievementSet.contains(AchievementType.NETWORK_SUPPORTER)) {
            newAchievements.add(createAchievement(AchievementType.NETWORK_SUPPORTER))
            userAchievementSet.add(AchievementType.NETWORK_SUPPORTER)
        }
        
        // Special combo achievements
        if (action.type == GameActionType.ENCRYPTED_MESSAGE_SENT && 
            action.type == GameActionType.P2P_CONNECTION_ESTABLISHED) {
            if (!userAchievementSet.contains(AchievementType.CRYPTO_NINJA)) {
                newAchievements.add(createAchievement(AchievementType.CRYPTO_NINJA))
                userAchievementSet.add(AchievementType.CRYPTO_NINJA)
            }
        }
        
        // Streak achievements
        if (stats.dailyStreakDays >= 7 && !userAchievementSet.contains(AchievementType.WEEKLY_WARRIOR)) {
            newAchievements.add(createAchievement(AchievementType.WEEKLY_WARRIOR))
            userAchievementSet.add(AchievementType.WEEKLY_WARRIOR)
        }
        
        if (newAchievements.isNotEmpty()) {
            _achievements.postValue(newAchievements)
            
            // Grant achievement rewards
            newAchievements.forEach { achievement ->
                grantRewards(userId, listOf(achievement.reward))
            }
        }
    }

    /**
     * Ödülleri hesaplar
     */
    private fun calculateRewards(action: GameAction, stats: UserGameStats): List<Reward> {
        val rewards = mutableListOf<Reward>()
        
        when (action.type) {
            GameActionType.MESSAGE_SENT -> {
                rewards.add(Reward(RewardType.MIS_COINS, 1))
                if (action.isEncrypted) {
                    rewards.add(Reward(RewardType.PRIVACY_POINTS, 2))
                }
            }
            
            GameActionType.ENCRYPTED_MESSAGE_SENT -> {
                rewards.add(Reward(RewardType.MIS_COINS, 2))
                rewards.add(Reward(RewardType.PRIVACY_POINTS, 5))
            }
            
            GameActionType.P2P_CONNECTION_ESTABLISHED -> {
                rewards.add(Reward(RewardType.MIS_COINS, 5))
                rewards.add(Reward(RewardType.REPUTATION_BOOST, 10))
            }
            
            GameActionType.FILE_SHARED_ENCRYPTED -> {
                rewards.add(Reward(RewardType.MIS_COINS, 3))
                rewards.add(Reward(RewardType.PRIVACY_POINTS, 10))
            }
            
            GameActionType.HELPED_NETWORK_ROUTING -> {
                rewards.add(Reward(RewardType.MIS_COINS, 10))
                rewards.add(Reward(RewardType.REPUTATION_BOOST, 15))
            }
            
            GameActionType.PRIVACY_SETTING_ENABLED -> {
                rewards.add(Reward(RewardType.PRIVACY_POINTS, 25))
            }
            
            GameActionType.DAILY_LOGIN -> {
                val streakMultiplier = min(stats.dailyStreakDays, 30)
                rewards.add(Reward(RewardType.MIS_COINS, 10 + streakMultiplier))
            }
        }
        
        return rewards
    }

    /**
     * Sosyal özellikler için puan hesaplama
     */
    fun calculateSocialScore(userId: String): SocialScore {
        val stats = getUserStats(userId)
        val achievements = userAchievements[userId]?.size ?: 0
        
        return SocialScore(
            totalScore = (stats.misCoins * 0.1 + stats.privacyPoints * 0.2 + 
                         stats.reputationScore * 100 + achievements * 50).toInt(),
            communicationScore = calculateCommunicationScore(stats),
            privacyScore = stats.privacyPoints,
            contributionScore = (stats.networkContributionHours * 10).toInt(),
            achievementScore = achievements * 50,
            level = calculateUserLevel(stats),
            nextLevelProgress = calculateLevelProgress(stats)
        )
    }

    /**
     * Leaderboard için kullanıcıları sıralar
     */
    private fun updateLeaderboards(userId: String, stats: UserGameStats) {
        // Global leaderboard update would happen here
        // For demo, we'll create a mock leaderboard
        val mockLeaderboard = listOf(
            LeaderboardEntry(userId, "Sen", calculateSocialScore(userId).totalScore, 1),
            LeaderboardEntry("user2", "Ahmet K.", 8500, 2),
            LeaderboardEntry("user3", "Ayşe M.", 7200, 3),
            LeaderboardEntry("user4", "Mehmet Y.", 6800, 4),
            LeaderboardEntry("user5", "Fatma S.", 6200, 5)
        )
        
        _leaderboard.postValue(mockLeaderboard)
    }

    /**
     * Özel etkinlikler ve sezonsal içerik
     */
    fun createSpecialEvent(): SpecialEvent {
        return SpecialEvent(
            id = "privacy_week_2024",
            title = "Gizlilik Haftası",
            description = "Bu hafta gizlilik odaklı aksiyonlar için 2x puan kazan!",
            startTime = System.currentTimeMillis(),
            endTime = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000), // 1 week
            multipliers = mapOf(
                GameActionType.ENCRYPTED_MESSAGE_SENT to 2.0,
                GameActionType.PRIVACY_SETTING_ENABLED to 3.0,
                GameActionType.FILE_SHARED_ENCRYPTED to 2.5
            ),
            specialRewards = listOf(
                Reward(RewardType.EXCLUSIVE_THEME, 1),
                Reward(RewardType.PRIVACY_BADGE, 1)
            )
        )
    }

    // Helper methods
    private fun initializeGamification() {
        gamificationScope.launch {
            // Initialize daily challenges
            generateDailyChallenges("current_user").also { challenges ->
                dailyChallenges.addAll(challenges)
                _dailyChallenges.postValue(challenges)
            }
        }
    }

    private fun getUserStats(userId: String): UserGameStats {
        return userStats.getOrPut(userId) { 
            UserGameStats(
                userId = userId,
                level = 1,
                experience = 0,
                misCoins = 100, // Starting coins
                privacyPoints = 50, // Starting privacy points
                reputationScore = 0.5
            )
        }
    }

    private fun updateUserStats(stats: UserGameStats, action: GameAction) {
        when (action.type) {
            GameActionType.MESSAGE_SENT -> {
                stats.messagesSent++
                stats.experience += 1
            }
            GameActionType.ENCRYPTED_MESSAGE_SENT -> {
                stats.messagesSent++
                stats.encryptedMessagesSent++
                stats.experience += 3
            }
            GameActionType.P2P_CONNECTION_ESTABLISHED -> {
                stats.p2pConnectionsEstablished++
                stats.experience += 5
            }
            GameActionType.FILE_SHARED_ENCRYPTED -> {
                stats.filesShared++
                stats.experience += 4
            }
            GameActionType.HELPED_NETWORK_ROUTING -> {
                stats.networkHelpCount++
                stats.experience += 8
            }
            GameActionType.PRIVACY_SETTING_ENABLED -> {
                stats.privacyActionsCount++
                stats.experience += 10
            }
            GameActionType.DAILY_LOGIN -> {
                stats.dailyLoginCount++
                stats.dailyStreakDays++
                stats.experience += 5
            }
        }
        
        // Level calculation
        val newLevel = calculateUserLevel(stats)
        if (newLevel > stats.level) {
            stats.level = newLevel
            // Level up bonus
            stats.misCoins += newLevel * 10
        }
    }

    private fun calculateUserLevel(stats: UserGameStats): Int {
        return (sqrt(stats.experience.toDouble()) / 10).toInt() + 1
    }

    private fun calculateLevelProgress(stats: UserGameStats): Float {
        val currentLevel = stats.level
        val expForCurrentLevel = (currentLevel - 1) * (currentLevel - 1) * 100
        val expForNextLevel = currentLevel * currentLevel * 100
        val progress = (stats.experience - expForCurrentLevel).toFloat() / (expForNextLevel - expForCurrentLevel)
        return progress.coerceIn(0f, 1f)
    }

    private fun calculateCommunicationScore(stats: UserGameStats): Int {
        return (stats.messagesSent * 2 + stats.encryptedMessagesSent * 5 + stats.filesShared * 3)
    }

    private fun createAchievement(type: AchievementType): Achievement {
        return when (type) {
            AchievementType.CHATTER -> Achievement(
                type = type,
                title = "Sohbet Uzmanı",
                description = "100 mesaj gönderdin!",
                icon = "ic_chat_achievement",
                reward = Reward(RewardType.MIS_COINS, 100),
                rarity = AchievementRarity.COMMON
            )
            AchievementType.SOCIAL_BUTTERFLY -> Achievement(
                type = type,
                title = "Sosyal Kelebek",
                description = "1000 mesaj gönderdin!",
                icon = "ic_social_achievement",
                reward = Reward(RewardType.MIS_COINS, 500),
                rarity = AchievementRarity.RARE
            )
            AchievementType.PRIVACY_ADVOCATE -> Achievement(
                type = type,
                title = "Gizlilik Savunucusu",
                description = "Gizlilik ayarlarını 10 kez güncelledin!",
                icon = "ic_privacy_achievement",
                reward = Reward(RewardType.PRIVACY_BADGE, 1),
                rarity = AchievementRarity.EPIC
            )
            AchievementType.NETWORK_SUPPORTER -> Achievement(
                type = type,
                title = "Ağ Destekçisi",
                description = "24 saat boyunca ağa katkıda bulundun!",
                icon = "ic_network_achievement",
                reward = Reward(RewardType.REPUTATION_BOOST, 100),
                rarity = AchievementRarity.LEGENDARY
            )
            AchievementType.CRYPTO_NINJA -> Achievement(
                type = type,
                title = "Kripto Ninja",
                description = "Şifrelenmiş mesaj gönderirken P2P bağlantı kurdun!",
                icon = "ic_crypto_achievement",
                reward = Reward(RewardType.EXCLUSIVE_THEME, 1),
                rarity = AchievementRarity.LEGENDARY
            )
            AchievementType.WEEKLY_WARRIOR -> Achievement(
                type = type,
                title = "Haftalık Savaşçı",
                description = "7 gün üst üste giriş yaptın!",
                icon = "ic_streak_achievement",
                reward = Reward(RewardType.MIS_COINS, 200),
                rarity = AchievementRarity.RARE
            )
        }
    }

    private fun grantRewards(userId: String, rewards: List<Reward>) {
        val stats = getUserStats(userId)
        
        rewards.forEach { reward ->
            when (reward.type) {
                RewardType.MIS_COINS -> stats.misCoins += reward.amount
                RewardType.PRIVACY_POINTS -> stats.privacyPoints += reward.amount
                RewardType.REPUTATION_BOOST -> stats.reputationScore += reward.amount / 1000.0
                RewardType.EXPERIENCE -> stats.experience += reward.amount
                RewardType.EXCLUSIVE_THEME -> stats.exclusiveThemes += reward.amount
                RewardType.PRIVACY_BADGE -> stats.privacyBadges += reward.amount
            }
        }
        
        _userStats.postValue(stats)
    }

    private fun updateDailyChallenges(userId: String, action: GameAction) {
        dailyChallenges.forEach { challenge ->
            when (challenge.type) {
                ChallengeType.SEND_MESSAGES -> {
                    if (action.type == GameActionType.MESSAGE_SENT) {
                        challenge.currentProgress = min(challenge.currentProgress + 1, challenge.targetValue)
                    }
                }
                ChallengeType.PRIVACY_ACTIONS -> {
                    if (action.type == GameActionType.PRIVACY_SETTING_ENABLED) {
                        challenge.currentProgress = min(challenge.currentProgress + 1, challenge.targetValue)
                    }
                }
                ChallengeType.NETWORK_CONTRIBUTION -> {
                    if (action.type == GameActionType.HELPED_NETWORK_ROUTING) {
                        challenge.currentProgress = min(challenge.currentProgress + action.value, challenge.targetValue)
                    }
                }
            }
            
            // Check if challenge is completed
            if (challenge.currentProgress >= challenge.targetValue && !challenge.isCompleted) {
                challenge.isCompleted = true
                grantRewards(userId, listOf(challenge.reward))
            }
        }
        
        _dailyChallenges.postValue(dailyChallenges)
    }

    private fun getEndOfDay(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        return calendar.timeInMillis
    }

    // Data classes
    data class UserGameStats(
        val userId: String,
        var level: Int = 1,
        var experience: Int = 0,
        var misCoins: Int = 0,
        var privacyPoints: Int = 0,
        var reputationScore: Double = 0.5,
        var messagesSent: Int = 0,
        var encryptedMessagesSent: Int = 0,
        var filesShared: Int = 0,
        var p2pConnectionsEstablished: Int = 0,
        var networkHelpCount: Int = 0,
        var privacyActionsCount: Int = 0,
        var dailyLoginCount: Int = 0,
        var dailyStreakDays: Int = 0,
        var networkContributionHours: Double = 0.0,
        var exclusiveThemes: Int = 0,
        var privacyBadges: Int = 0
    )

    data class GameAction(
        val type: GameActionType,
        val value: Int = 1,
        val isEncrypted: Boolean = false,
        val metadata: Map<String, Any> = emptyMap()
    )

    data class Achievement(
        val type: AchievementType,
        val title: String,
        val description: String,
        val icon: String,
        val reward: Reward,
        val rarity: AchievementRarity,
        val unlockedAt: Long = System.currentTimeMillis()
    )

    data class DailyChallenge(
        val id: String,
        val type: ChallengeType,
        val title: String,
        val description: String,
        val targetValue: Int,
        var currentProgress: Int = 0,
        val reward: Reward,
        val expiresAt: Long,
        var isCompleted: Boolean = false
    )

    data class Reward(
        val type: RewardType,
        val amount: Int
    )

    data class SocialScore(
        val totalScore: Int,
        val communicationScore: Int,
        val privacyScore: Int,
        val contributionScore: Int,
        val achievementScore: Int,
        val level: Int,
        val nextLevelProgress: Float
    )

    data class LeaderboardEntry(
        val userId: String,
        val displayName: String,
        val score: Int,
        val rank: Int
    )

    data class SpecialEvent(
        val id: String,
        val title: String,
        val description: String,
        val startTime: Long,
        val endTime: Long,
        val multipliers: Map<GameActionType, Double>,
        val specialRewards: List<Reward>
    )

    // Enums
    enum class GameActionType {
        MESSAGE_SENT,
        ENCRYPTED_MESSAGE_SENT,
        P2P_CONNECTION_ESTABLISHED,
        FILE_SHARED_ENCRYPTED,
        HELPED_NETWORK_ROUTING,
        PRIVACY_SETTING_ENABLED,
        DAILY_LOGIN
    }

    enum class AchievementType {
        CHATTER,
        SOCIAL_BUTTERFLY,
        PRIVACY_ADVOCATE,
        NETWORK_SUPPORTER,
        CRYPTO_NINJA,
        WEEKLY_WARRIOR
    }

    enum class AchievementRarity {
        COMMON, RARE, EPIC, LEGENDARY
    }

    enum class ChallengeType {
        SEND_MESSAGES,
        PRIVACY_ACTIONS,
        NETWORK_CONTRIBUTION
    }

    enum class RewardType {
        MIS_COINS,
        PRIVACY_POINTS,
        REPUTATION_BOOST,
        EXPERIENCE,
        EXCLUSIVE_THEME,
        PRIVACY_BADGE
    }
}