package sikv.lingomate.data.chat.service

import sikv.lingomate.data.chat.domain.ChatLanguage

class StartChatService {

    // TODO: Implement persistent storage.
    private var selectedLanguage = ChatLanguage.ENGLISH

    suspend fun getChatLanguages(): List<ChatLanguage> {
        return ChatLanguage.entries
    }

    suspend fun getSelectedLanguage(): ChatLanguage? {
        return selectedLanguage
    }

    fun selectLanguage(selectedLanguage: ChatLanguage) {
        this.selectedLanguage = selectedLanguage
    }
}
