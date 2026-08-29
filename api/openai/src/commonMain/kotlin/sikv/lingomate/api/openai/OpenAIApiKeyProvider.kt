package sikv.lingomate.api.openai

fun interface OpenAIApiKeyProvider {
    fun getApiKey(): String?
}
