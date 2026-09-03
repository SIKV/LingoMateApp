package sikv.lingomate.api.config

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.dsl.module

// TODO: Add URL.
private const val CONFIG_URL = ""

val configApiModule = module {

    single {
        ConfigApi(
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
