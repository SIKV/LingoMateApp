package sikv.lingomate.data.config.datasource

import sikv.lingomate.api.config.ConfigApi
import sikv.lingomate.api.config.model.ConfigDTO
import sikv.lingomate.data.config.domain.Config
import sikv.lingomate.data.config.domain.ConfigChatModel

class RemoteConfigDataSource(
    private val configApi: ConfigApi
) {
    suspend fun getConfig(): Config? {
        return configApi.getConfig()
            .getOrNull()
            ?.toConfig()
    }
}

private fun ConfigDTO.toConfig(): Config {
    return Config(
        chatModels = chatModels.map { chatModel ->
            ConfigChatModel(
                provider = chatModel.provider,
                model = chatModel.model
            )
        },
        languageCodes = languages
    )
}
