package com.mis.communication.constants

/**
 * Bildirim sabitleri
 */
object NotificationConstants {
    
    // Notification Channel IDs
    const val CHANNEL_MESSAGES = "messages_channel"
    const val CHANNEL_CALLS = "calls_channel"
    const val CHANNEL_STATUS = "status_channel"
    const val CHANNEL_SYSTEM = "system_channel"
    
    // Notification IDs
    const val NOTIFICATION_ID_MESSAGE = 1001
    const val NOTIFICATION_ID_CALL = 1002
    const val NOTIFICATION_ID_STATUS = 1003
    const val NOTIFICATION_ID_SYSTEM = 1004
    
    // Actions
    const val ACTION_REPLY = "action_reply"
    const val ACTION_MARK_READ = "action_mark_read"
    const val ACTION_ANSWER_CALL = "action_answer_call"
    const val ACTION_DECLINE_CALL = "action_decline_call"
    
    // Extras
    const val EXTRA_MESSAGE_ID = "extra_message_id"
    const val EXTRA_CHAT_ID = "extra_chat_id"
    const val EXTRA_CALL_ID = "extra_call_id"
    const val EXTRA_REPLY_TEXT = "extra_reply_text"
}