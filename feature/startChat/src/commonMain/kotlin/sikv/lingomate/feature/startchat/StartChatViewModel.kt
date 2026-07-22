package sikv.lingomate.feature.startchat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.PracticeLanguage
import sikv.lingomate.data.chat.domain.PracticeType
import sikv.lingomate.data.chat.domain.AssistantLanguage
import sikv.lingomate.data.chat.service.StartChatService
import kotlin.native.ObjCName

@ObjCName("StartChatViewModel", exact = true)
class StartChatViewModel(
    private val startChatService: StartChatService
) : ViewModel() {

    private val _uiState = MutableStateFlow(StartChatState.empty())
    @NativeCoroutinesState
    val uiState: StateFlow<StartChatState> = _uiState.asStateFlow()

    init {
        initState()
    }

    private fun initState() {
        viewModelScope.launch {
            _uiState.update {
                StartChatState(
                    chatModels = startChatService.getChatModels(),
                    selectedChatModel = startChatService.getSelectedChatModel(),
                    practiceLanguages = startChatService.getPracticeLanguages(),
                    selectedPracticeLanguage = startChatService.getSelectedPracticeLanguage(),
                    assistantLanguages = startChatService.getAssistantLanguages(),
                    selectedAssistantLanguage = startChatService.getSelectedAssistantLanguage(),
                    practiceTypes = startChatService.getPracticeTypes(),
                    selectedPracticeType = startChatService.getSelectedPracticeType()
                )
            }
        }
    }

    fun selectChatModel(chatModel: ChatModel) {
        startChatService.selectChatModel(chatModel)

        _uiState.update {
            it.copy(selectedChatModel = chatModel)
        }
    }

    fun selectPracticeLanguage(practiceLanguage: PracticeLanguage) {
        startChatService.selectPracticeLanguage(practiceLanguage)

        _uiState.update {
            it.copy(selectedPracticeLanguage = practiceLanguage)
        }
    }

    fun selectAssistantLanguage(assistantLanguage: AssistantLanguage) {
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
}
