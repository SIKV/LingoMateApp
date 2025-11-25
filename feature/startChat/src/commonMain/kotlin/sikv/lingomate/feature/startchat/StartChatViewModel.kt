package sikv.lingomate.feature.startchat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatLength
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
        // TODO: Verify it works correctly in SwiftUI.
        viewModelScope.launch {
            try {
                coroutineScope {
                    val languages = async { startChatService.getChatLanguages() }
                    val selectedLanguage = async { startChatService.getSelectedLanguage() }
                    val lengths = async { startChatService.getChatLengths() }
                    val selectedLength = async { startChatService.getSelectedLength() }

                    _uiState.update {
                        StartChatState(
                            chatLanguages = languages.await(),
                            selectedLanguage = selectedLanguage.await(),
                            chatLengths = lengths.await(),
                            selectedLength = selectedLength.await()
                        )
                    }
                }
            } catch (e: Exception) {
                // TODO: Handle.
            }
        }
    }

    fun selectLanguage(selectedLanguage: ChatLanguage) {
        startChatService.selectLanguage(selectedLanguage)

        _uiState.update {
            it.copy(selectedLanguage = selectedLanguage)
        }
    }

    fun selectLength(selectedLength: ChatLength) {
        startChatService.selectLength(selectedLength)

        _uiState.update {
            it.copy(selectedLength = selectedLength)
        }
    }
}
