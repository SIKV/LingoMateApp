import SwiftUI

enum L10n {
    static let tabChat = LocalizedStringKey("tab_chat")
    static let tabMore = LocalizedStringKey("tab_more")
    
    static let languageArabic = LocalizedStringKey("language_arabic")
    static let languageCzech = LocalizedStringKey("language_czech")
    static let languageDanish = LocalizedStringKey("language_danish")
    static let languageDutch = LocalizedStringKey("language_dutch")
    static let languageEnglish = LocalizedStringKey("language_english")
    static let languageFinnish = LocalizedStringKey("language_finnish")
    static let languageFrench = LocalizedStringKey("language_french")
    static let languageGerman = LocalizedStringKey("language_german")
    static let languageGreek = LocalizedStringKey("language_greek")
    static let languageHungarian = LocalizedStringKey("language_hungarian")
    static let languageItalian = LocalizedStringKey("language_italian")
    static let languageJapanese = LocalizedStringKey("language_japanese")
    static let languageKorean = LocalizedStringKey("language_korean")
    static let languageNorwegian = LocalizedStringKey("language_norwegian")
    static let languagePolish = LocalizedStringKey("language_polish")
    static let languagePortuguese = LocalizedStringKey("language_portuguese")
    static let languageRomanian = LocalizedStringKey("language_romanian")
    static let languageSpanish = LocalizedStringKey("language_spanish")
    static let languageSwedish = LocalizedStringKey("language_swedish")
    static let languageTurkish = LocalizedStringKey("language_turkish")
    static let languageUkrainian = LocalizedStringKey("language_ukrainian")
    static let practiceTypeConversation = LocalizedStringKey("practice_type_conversation")
    static let practiceTypeTranslation = LocalizedStringKey("practice_type_translation")
    static let chatModelProviderOnDevice = LocalizedStringKey("chat_model_provider_on_device")

    static let startChatGreeting = LocalizedStringKey("start_chat_greeting")
    static let startChatInfo = LocalizedStringKey("start_chat_info")
    static let startChatStartButton = LocalizedStringKey("start_chat_start_button")
    static let startChatChatModelLabel = LocalizedStringKey("start_chat_chat_model_label")
    static let startChatPracticeLanguageLabel = LocalizedStringKey("start_chat_practice_language_label")
    static let startChatAssistantLanguageLabel = LocalizedStringKey("start_chat_assistant_language_label")
    static let startChatPracticeTypeLabel = LocalizedStringKey("start_chat_practice_type_label")
    static let startChatNotSelected = LocalizedStringKey("start_chat_not_selected")
    static let startChatNoApiKey = LocalizedStringKey("start_chat_no_api_key")
    static let startChatApiKeyHint = LocalizedStringKey("start_chat_api_key_hint")
    
    static let chatMessageStatusDelivered = LocalizedStringKey("chat_message_status_delivered")
    static let chatMessageStatusUserFailed = LocalizedStringKey("chat_message_status_user_failed")
    static let chatMessageStatusAssistantFailed = LocalizedStringKey("chat_message_status_assistant_failed")
    static let chatTypeMessageHint = LocalizedStringKey("chat_type_message_hint")

    static let moreTitle = LocalizedStringKey("more_title")
    static let moreManageApiKeysTitle = LocalizedStringKey("more_manage_api_keys_title")
    static let moreManageApiKeysSubtitle = LocalizedStringKey("more_manage_api_keys_subtitle")

    static let manageApiKeysTitle = LocalizedStringKey("manage_api_keys_title")
    static let manageApiKeysKeySaved = LocalizedStringKey("manage_api_keys_key_saved")
    static let manageApiKeysListFooter = LocalizedStringKey("manage_api_keys_list_footer")
    static let manageApiKeysEmptyTitle = LocalizedStringKey("manage_api_keys_empty_title")
    static let manageApiKeysEmptyInfo = LocalizedStringKey("manage_api_keys_empty_info")
    static let manageApiKeysAddButton = LocalizedStringKey("manage_api_keys_add_button")
    static let manageApiKeysAddTitle = LocalizedStringKey("manage_api_keys_add_title")
    static let manageApiKeysReplaceTitle = LocalizedStringKey("manage_api_keys_replace_title")
    static let manageApiKeysProviderLabel = LocalizedStringKey("manage_api_keys_provider_label")
    static let manageApiKeysKeyLabel = LocalizedStringKey("manage_api_keys_key_label")
    static let manageApiKeysKeyFooter = LocalizedStringKey("manage_api_keys_key_footer")
    static let manageApiKeysShowKey = LocalizedStringKey("manage_api_keys_show_key")
    static let manageApiKeysHideKey = LocalizedStringKey("manage_api_keys_hide_key")
    static let manageApiKeysSaveButton = LocalizedStringKey("manage_api_keys_save_button")
    static let manageApiKeysCancelButton = LocalizedStringKey("manage_api_keys_cancel_button")
    static let manageApiKeysDeleteButton = LocalizedStringKey("manage_api_keys_delete_button")
    static let manageApiKeysReplaceConfirmTitle = LocalizedStringKey("manage_api_keys_replace_confirm_title")
    static let manageApiKeysReplaceConfirmButton = LocalizedStringKey("manage_api_keys_replace_confirm_button")
    static let manageApiKeysDeleteConfirmTitle = LocalizedStringKey("manage_api_keys_delete_confirm_title")

    /// Confirmation body naming the provider whose key is about to be overwritten.
    static func manageApiKeysReplaceConfirmMessage(_ provider: String) -> String {
        String(
            format: String(localized: "manage_api_keys_replace_confirm_message"),
            provider
        )
    }

    /// Confirmation body naming the provider whose key is about to be deleted.
    static func manageApiKeysDeleteConfirmMessage(_ provider: String) -> String {
        String(
            format: String(localized: "manage_api_keys_delete_confirm_message"),
            provider
        )
    }
}
