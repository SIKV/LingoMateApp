import Combine
import SwiftUI
import Shared
import KMPNativeCoroutinesAsync

@MainActor
class StartChatVM: ObservableObject {
    private let viewModel = ViewModels().getStartChatViewModel()

    @Published var state = StartChatState.companion.empty()

    // uiState is a StateFlow, so the only error is CancellationError from cancel(), which needs no handling.
    private var stateTask: Task<Void, Error>?

    /// Complete configuration once every field has been selected.
    var chatConfig: ChatConfig? {
        guard
            let chatModel = state.selectedChatModelOption?.chatModel,
            let practiceLanguage = state.selectedPracticeLanguage,
            let assistantLanguage = state.selectedAssistantLanguage,
            let practiceType = state.selectedPracticeType
        else {
            return nil
        }

        return ChatConfig(
            chatModel: chatModel,
            practiceLanguage: practiceLanguage,
            assistantLanguage: assistantLanguage,
            practiceType: practiceType
        )
    }

    func listenState() {
        if stateTask != nil {
            return
        }

        stateTask = Task {
            for try await state in asyncSequence(for: viewModel.uiStateFlow) {
                self.state = state
            }
        }
    }

    func selectChatModel(_ chatModelOption: ChatModelOption) {
        viewModel.selectChatModel(chatModelOption: chatModelOption)
    }

    func selectPracticeLanguage(_ practiceLanguage: Language) {
        viewModel.selectPracticeLanguage(practiceLanguage: practiceLanguage)
    }

    func selectAssistantLanguage(_ assistantLanguage: Language) {
        viewModel.selectAssistantLanguage(assistantLanguage: assistantLanguage)
    }

    func selectPracticeType(_ practiceType: PracticeType) {
        viewModel.selectPracticeType(practiceType: practiceType)
    }

    func cancel() {
        stateTask?.cancel()
        stateTask = nil

        viewModel.onCleared()
    }
}
