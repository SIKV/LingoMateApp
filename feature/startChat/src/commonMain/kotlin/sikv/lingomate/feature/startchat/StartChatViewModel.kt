package sikv.lingomate.feature.startchat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sikv.lingomate.data.apikeystorage.ApiKeyStorage
import sikv.lingomate.data.chat.domain.Language
import sikv.lingomate.data.chat.domain.PracticeType
import sikv.lingomate.data.chat.service.StartChatService
import kotlin.native.ObjCName

@ObjCName("StartChatViewModel", exact = true)
class StartChatViewModel(
    private val startChatService: StartChatService,
    private val apiKeyStorage: ApiKeyStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(StartChatState.empty())
    @NativeCoroutinesState
    val uiState: StateFlow<StartChatState> = _uiState.asStateFlow()

    init {
        initState()
    }

    fun selectChatModel(chatModelOption: ChatModelOption) {
        startChatService.selectChatModel(chatModelOption.chatModel)

        _uiState.update {
            it.copy(selectedChatModelOption = chatModelOption)
        }
    }

    fun selectPracticeLanguage(practiceLanguage: Language) {
        startChatService.selectPracticeLanguage(practiceLanguage)

        _uiState.update {
            it.copy(selectedPracticeLanguage = practiceLanguage)
        }
    }

    fun selectAssistantLanguage(assistantLanguage: Language) {
        startChatService.selectAssistantLanguage(assistantLanguage)

        _uiState.update {
            it.copy(selectedAssistantLanguage = assistantLanguage)
        }
    }

    fun selectPracticeType(practiceType: PracticeType) {
        startChatService.selectPracticeType(practiceType)

        _uiState.update {
            it.copy(selectedPracticeType = practiceType)
        }
    }

    private fun initState() {
        viewModelScope.launch {
            _uiState.update {
                StartChatState(
                    chatModelOptions = emptyList(),
                    selectedChatModelOption = null,
                    practiceLanguages = startChatService.getPracticeLanguages(),
                    selectedPracticeLanguage = startChatService.getSelectedPracticeLanguage(),
                    assistantLanguages = startChatService.getAssistantLanguages(),
                    selectedAssistantLanguage = startChatService.getSelectedAssistantLanguage(),
                    practiceTypes = startChatService.getPracticeTypes(),
                    selectedPracticeType = startChatService.getSelectedPracticeType()
                )
            }

            flowStoredProviders()
        }
    }

    private suspend fun flowStoredProviders() {
        apiKeyStorage.flowStoredProviders()
            .collect { storedProviders ->
                val chatModels = startChatService.getChatModels()
                val selectedChatModel = startChatService.getSelectedChatModel()

                val chatModelOptions = chatModels.map { chatModel ->
                    val apiKeyNeeded = if (chatModel.provider.apiKeyRequired) {
                        !storedProviders.contains(chatModel.provider.toApiKeyProvider())
                    } else {
                        false
                    }

                    ChatModelOption(
                        chatModel = chatModel,
                        apiKeyNeeded = apiKeyNeeded
                    )
                }

                _uiState.update { state ->
                    val selectedChatModelOption = chatModelOptions
                        .find { it.chatModel == selectedChatModel }
                        // Reset the selected chat model if it needs an api key.
                        ?.takeIf { !it.apiKeyNeeded  }

                    state.copy(
                        chatModelOptions = chatModelOptions,
                        selectedChatModelOption = selectedChatModelOption
                    )
                }
            }
    }
}
