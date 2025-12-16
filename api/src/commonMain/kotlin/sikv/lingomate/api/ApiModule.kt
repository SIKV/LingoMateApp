package sikv.lingomate.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.sse.SSE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

val apiModule = module {

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
            install(SSE) {
                maxReconnectionAttempts = 3
                reconnectionTime = 2.seconds
            }
        }
    }

    single {
        Json {
            ignoreUnknownKeys = true
        }
    }

    single {
        OpenAIApi(
            client = get(),
            json = get()
        )
    }
}
