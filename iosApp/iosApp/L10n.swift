import SwiftUI

enum L10n {
    static let tabChat = LocalizedStringKey("tab_chat")
    static let tabHistory = LocalizedStringKey("tab_history")
    static let tabMore = LocalizedStringKey("tab_more")
    
    static let chatLanguageEnglish = LocalizedStringKey("chat_language_english")
    static let chatLanguageSpanish = LocalizedStringKey("chat_language_spanish")
    static let translationLanguageEnglish = LocalizedStringKey("translation_language_english")
    static let practiceTypeConversation = LocalizedStringKey("practice_type_conversation")
    static let practiceTypeTranslation = LocalizedStringKey("practice_type_translation")
    static let chatModelProviderOnDevice = LocalizedStringKey("chat_model_provider_on_device")

    static let startChatGreeting = LocalizedStringKey("start_chat_greeting")
    static let startChatInfo = LocalizedStringKey("start_chat_info")
    static let startChatStartButton = LocalizedStringKey("start_chat_start_button")
    static let startChatChatModelLabel = LocalizedStringKey("start_chat_chat_model_label")
    static let startChatPracticeLanguageLabel = LocalizedStringKey("start_chat_practice_language_label")
    static let startChatTranslationLanguageLabel = LocalizedStringKey("start_chat_translation_language_label")
    static let startChatPracticeTypeLabel = LocalizedStringKey("start_chat_practice_type_label")
    static let startChatNotSelected = LocalizedStringKey("start_chat_not_selected")
    
    static let chatMessageStatusDelivered = LocalizedStringKey("chat_message_status_delivered")
    static let chatMessageStatusUserFailed = LocalizedStringKey("chat_message_status_user_failed")
    static let chatMessageStatusAssistantFailed = LocalizedStringKey("chat_message_status_assistant_failed")
    static let chatTypeMessageHint = LocalizedStringKey("chat_type_message_hint")
}
