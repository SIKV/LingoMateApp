package sikv.lingomate.data.config

import sikv.lingomate.data.config.datasource.FallbackConfigDataSource
import sikv.lingomate.data.config.datasource.RemoteConfigDataSource
import sikv.lingomate.data.config.domain.Config
import sikv.lingomate.data.config.domain.withFallback
import sikv.lingomate.logger.Log

class ConfigRepository(
    private val remoteConfigDataSource: RemoteConfigDataSource,
    private val fallbackConfigDataSource: FallbackConfigDataSource
) {

    // TODO: Implement persistent storage, so a launch with no network can still use the
    //  config read the last time there was one.

    private var config: Config? = null

    suspend fun getConfig(): Config {
        config?.let { return it }

        val fallbackConfig = fallbackConfigDataSource.getConfig()
        val remoteConfig = remoteConfigDataSource.getConfig()

        if (remoteConfig == null) {
            Log.w { "Falling back to the config the app ships with." }
            return fallbackConfig
        }

        return remoteConfig.withFallback(fallbackConfig)
            .also { config = it }
    }
}
