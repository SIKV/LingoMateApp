package sikv.lingomate.data.config.datasource

import sikv.lingomate.data.config.domain.Config
import sikv.lingomate.data.config.domain.ConfigChatModel

class FallbackConfigDataSource {

    fun getConfig(): Config {
        return Config(
            chatModels = listOf(
                ConfigChatModel(
                    provider = "OPEN_AI",
                    model = "gpt-5-mini"
                )
            ),
            languageCodes = listOf(
                "ar", "cs", "da", "nl", "en", "fi", "fr", "de", "el", "hu", "it",
                "ja", "ko", "no", "pl", "pt", "ro", "es", "sv", "tr", "uk"
            )
        )
    }
}
