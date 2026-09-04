package sikv.lingomate.data.config.datasource

import sikv.lingomate.api.remoteconfig.RemoteConfigApi
import sikv.lingomate.api.remoteconfig.model.RemoteConfigDTO
import sikv.lingomate.data.config.domain.Config
import sikv.lingomate.data.config.domain.ConfigChatModel

class RemoteConfigDataSource(
    private val remoteConfigApi: RemoteConfigApi
) {
    suspend fun getConfig(): Config? {
        return remoteConfigApi.getConfig()
            .getOrNull()
            ?.toConfig()
    }
}

private fun RemoteConfigDTO.toConfig(): Config {
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
