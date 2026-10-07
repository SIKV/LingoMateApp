package sikv.lingomate.api.openai

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.sse.SSE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private const val RESPONSES_URL = "https://api.openai.com/v1/responses"

private val TIMEOUT = 1.minutes

val openaiApiModule = module {

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
            install(SSE) {
                maxReconnectionAttempts = 3
                reconnectionTime = 2.seconds
            }
            install(HttpTimeout) {
                // Ktor never applies the request timeout to SSE, so these two are what limit the stream.
                connectTimeoutMillis = TIMEOUT.inWholeMilliseconds
                socketTimeoutMillis = TIMEOUT.inWholeMilliseconds
            }
        }
    }

    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        }
    }

    single {
        OpenAIApi(
            client = get(),
            json = get(),
            apiKeyProvider = get(),
            responsesUrl = RESPONSES_URL
        )
    }
}
