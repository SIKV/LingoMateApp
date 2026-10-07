package sikv.lingomate.data.config.mapping

import sikv.lingomate.data.config.domain.CachedChatModel
import sikv.lingomate.data.config.domain.CachedConfig
import sikv.lingomate.data.config.domain.Config
import sikv.lingomate.data.config.domain.ConfigChatModel

internal fun CachedConfig.toConfig(): Config {
    return Config(
        chatModels = chatModels.map { chatModel ->
            ConfigChatModel(
                provider = chatModel.provider,
                model = chatModel.model,
                tier = chatModel.tier
            )
        },
        languageCodes = languageCodes
    )
}

internal fun Config.toCachedConfig(): CachedConfig {
    return CachedConfig(
        chatModels = chatModels.map { chatModel ->
            CachedChatModel(
                provider = chatModel.provider,
                model = chatModel.model,
                tier = chatModel.tier
            )
        },
        languageCodes = languageCodes
    )
}
