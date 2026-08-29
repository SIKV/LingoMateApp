package sikv.lingomate.di

import org.koin.dsl.module
import sikv.lingomate.api.openai.OpenAIApiKeyProvider
import sikv.lingomate.data.apikeystorage.ApiKeyProvider
import sikv.lingomate.data.apikeystorage.ApiKeyStorage

val apiKeyProviderModule = module {

    single<OpenAIApiKeyProvider> {
        val apiKeyStorage = get<ApiKeyStorage>()
        OpenAIApiKeyProvider {
            apiKeyStorage.getApiKey(ApiKeyProvider.OpenAI)
        }
    }
}
