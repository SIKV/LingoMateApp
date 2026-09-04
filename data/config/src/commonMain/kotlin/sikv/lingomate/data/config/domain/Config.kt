package sikv.lingomate.data.config.domain

data class Config(
    val chatModels: List<ConfigChatModel> = emptyList(),
    val languageCodes: List<String> = emptyList()
)
