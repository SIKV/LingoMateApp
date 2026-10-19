package sikv.lingomate.analytics

import kotlin.native.ObjCName

@ObjCName("AnalyticsEvent", exact = true)
sealed interface AnalyticsEvent {
    val name: String
    val properties: Map<String, Any> get() = emptyMap()

    data class ChatStarted(
        val chatModelProvider: String,
        val chatModel: String,
        val practiceLanguage: String,
        val assistantLanguage: String,
        val practiceType: String,
    ) : AnalyticsEvent {
        override val name = "chat_started"
        override val properties = mapOf(
            "chat_model_provider" to chatModelProvider,
            "chat_model" to chatModel,
            "practice_language" to practiceLanguage,
            "assistant_language" to assistantLanguage,
            "practice_type" to practiceType,
        )
    }

    data object MessageSent : AnalyticsEvent {
        override val name = "message_sent"
    }

    data class ChatRecapOpened(val userMessageCount: Int) : AnalyticsEvent {
        override val name = "chat_recap_opened"
        override val properties = mapOf<String, Any>("user_message_count" to userMessageCount)
    }

    data class ApiKeyAdded(val provider: String) : AnalyticsEvent {
        override val name = "api_key_added"
        override val properties = mapOf("provider" to provider)
    }

    data class ApiKeyRemoved(val provider: String) : AnalyticsEvent {
        override val name = "api_key_removed"
        override val properties = mapOf("provider" to provider)
    }
}
