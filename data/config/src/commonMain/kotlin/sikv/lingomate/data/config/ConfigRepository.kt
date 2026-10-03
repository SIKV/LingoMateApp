package sikv.lingomate.data.config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sikv.lingomate.data.config.datasource.CachedConfigDataSource
import sikv.lingomate.data.config.datasource.FallbackConfigDataSource
import sikv.lingomate.data.config.datasource.RemoteConfigDataSource
import sikv.lingomate.data.config.domain.Config
import sikv.lingomate.data.config.domain.withFallback
import sikv.lingomate.logger.Log

class ConfigRepository(
    private val remoteConfigDataSource: RemoteConfigDataSource,
    private val cachedConfigDataSource: CachedConfigDataSource,
    private val fallbackConfigDataSource: FallbackConfigDataSource,
    // Runs the refresh of the cached config, which outlives the call that starts it.
    private val refreshScope: CoroutineScope
) {

    private var config: Config? = null

    suspend fun getConfig(): Config {
        config?.let { return it }

        val fallbackConfig = fallbackConfigDataSource.getConfig()

        // Start with what the remote served last time and refresh it for the next launch, so
        // the config never changes while the app is running.
        val cachedConfig = cachedConfigDataSource.getConfig()

        if (cachedConfig != null) {
            refreshScope.launch { refreshCachedConfig() }

            return cachedConfig.withFallback(fallbackConfig)
                .also { config = it }
        }

        // Nothing is cached until the first fetch succeeds, so only then wait for the remote.
        val remoteConfig = remoteConfigDataSource.getConfig()

        if (remoteConfig == null) {
            Log.w { "Falling back to the config the app ships with." }
            return fallbackConfig
        }

        // Cache it as served, so the fallback that fills its gaps is always the current build's.
        cachedConfigDataSource.setConfig(remoteConfig)

        return remoteConfig.withFallback(fallbackConfig)
            .also { config = it }
    }

    private suspend fun refreshCachedConfig() {
        val remoteConfig = remoteConfigDataSource.getConfig() ?: return

        cachedConfigDataSource.setConfig(remoteConfig)
    }
}
