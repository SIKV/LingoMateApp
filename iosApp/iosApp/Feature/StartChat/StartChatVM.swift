import Combine
import SwiftUI
import Shared
import KMPNativeCoroutinesAsync

@MainActor
class StartChatVM: ObservableObject {
    private let viewModel = ViewModels().getStartChatViewModel()

    @Published var state = StartChatState.companion.empty()

    private var stateTask: Task<Void, Never>?

    /// Complete configuration once every field has been selected.
    var chatConfig: ChatConfig? {
        guard
            let chatModel = state.selectedChatModel,
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
            do {
                for try await state in asyncSequence(for: viewModel.uiStateFlow) {
                    self.state = state
                }
            } catch {
                // FIXME: Handle.
                print("Failed with error: \(error)")
            }
        }
    }

    func selectChatModel(_ chatModel: ChatModel) {
        viewModel.selectChatModel(chatModel: chatModel)
    }

    func selectPracticeLanguage(_ practiceLanguage: PracticeLanguage) {
        viewModel.selectPracticeLanguage(practiceLanguage: practiceLanguage)
    }

    func selectAssistantLanguage(_ assistantLanguage: AssistantLanguage) {
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
