package sikv.lingomate.api.remoteconfig

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private const val CONFIG_URL = "https://sikv.github.io/LingoMateApp/v1/remote_config.json"

val remoteConfigApiModule = module {

    single {
        RemoteConfigApi(
            client = HttpClient(),
            json = Json {
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
            },
            configUrl = CONFIG_URL
        )
    }
}
