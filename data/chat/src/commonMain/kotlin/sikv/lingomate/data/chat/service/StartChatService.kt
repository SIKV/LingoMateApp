package sikv.lingomate.data.chat.service

import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatLength

class StartChatService {

    // TODO: Implement persistent storage.
    private var selectedLanguage = ChatLanguage.ENGLISH
    private var selectedLength = ChatLength.MEDIUM_LENGTH

    suspend fun getChatLanguages(): List<ChatLanguage> {
        return ChatLanguage.entries
    }

    suspend fun getSelectedLanguage(): ChatLanguage? {
        return selectedLanguage
    }

    fun selectLanguage(selectedLanguage: ChatLanguage) {
        this.selectedLanguage = selectedLanguage
    }

    suspend fun getChatLengths(): List<ChatLength> {
        return ChatLength.entries
    }

    suspend fun getSelectedLength(): ChatLength? {
        return selectedLength
    }

    fun selectLength(selectedLength: ChatLength) {
        this.selectedLength = selectedLength
    }
}
