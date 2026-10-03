package sikv.lingomate.data.config.datasource

import kotlinx.serialization.json.Json
import sikv.lingomate.data.config.domain.CachedConfig
import sikv.lingomate.data.config.domain.Config
import sikv.lingomate.data.config.mapping.toCachedConfig
import sikv.lingomate.data.config.mapping.toConfig
import sikv.lingomate.data.keyvaluestorage.KeyValueStorage
import sikv.lingomate.logger.Log

/**
 * Keeps the last config read from the remote, so a launch can start with it instead of
 * waiting for the network.
 */
class CachedConfigDataSource(
    private val keyValueStorage: KeyValueStorage
) {

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getConfig(): Config? {
        val cachedConfig = keyValueStorage.get(KEY_CONFIG) ?: return null

        return try {
            json.decodeFromString<CachedConfig>(cachedConfig).toConfig()
        } catch (e: IllegalArgumentException) {
            // Covers SerializationException too. The next successful fetch caches a fresh copy.
            Log.e(e) { "Dropping the cached config, it cannot be read." }
            keyValueStorage.remove(KEY_CONFIG)
            null
        }
    }

    suspend fun setConfig(config: Config) {
        keyValueStorage.put(KEY_CONFIG, json.encodeToString(config.toCachedConfig()))
    }

    private companion object {
        const val KEY_CONFIG = "config.cached_config"
    }
}
