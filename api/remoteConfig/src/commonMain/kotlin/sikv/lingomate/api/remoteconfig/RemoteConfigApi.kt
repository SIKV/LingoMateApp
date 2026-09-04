package sikv.lingomate.api.remoteconfig

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import sikv.lingomate.api.remoteconfig.model.RemoteConfigDTO
import sikv.lingomate.logger.Log

/**
 * Reads the remotely hosted app config. It holds no secrets, so the request carries
 * no credentials.
 */
class RemoteConfigApi(
    private val client: HttpClient,
    private val json: Json,
    private val configUrl: String
) {
    suspend fun getConfig(): Result<RemoteConfigDTO> {
        Log.d { "Requesting the config." }

        return try {
            val response = client.get(configUrl)

            // A missing file is served as an HTML error page, so never parse a failed response.
            if (!response.status.isSuccess()) {
                Log.e { "Config request failed with the status ${response.status}." }
                return Result.failure(RemoteConfigRequestException(response.status.value))
            }

            Result.success(json.decodeFromString<RemoteConfigDTO>(response.bodyAsText()))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(e) { "Failed to read the config." }
            Result.failure(e)
        }
    }
}
