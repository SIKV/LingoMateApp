package sikv.lingomate.feature.startchat

import sikv.lingomate.data.apikeystorage.ApiKeyProvider
import sikv.lingomate.data.chat.domain.ChatModelProvider

internal fun ChatModelProvider.toApiKeyProvider(): ApiKeyProvider? {
    return when (this) {
        ChatModelProvider.ON_DEVICE -> null
        ChatModelProvider.OPEN_AI -> ApiKeyProvider.OpenAI
    }
}
