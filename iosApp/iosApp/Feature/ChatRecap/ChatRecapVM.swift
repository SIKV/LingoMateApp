import Combine
import SwiftUI
import Shared
import KMPNativeCoroutinesAsync

@MainActor
class ChatRecapVM: ObservableObject {
    @Published var state = ChatRecapState.companion.empty()

    // uiState is a StateFlow, so the only error is CancellationError from cancel(), which needs no handling.
    private var stateTask: Task<Void, Error>?
    private let viewModel: ChatRecapViewModel

    init(chatConfig: ChatConfig, messages: [ChatMessage]) {
        viewModel = ViewModels().getChatRecapViewModel(
            chatConfig: chatConfig,
            messages: messages
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

    func retry() {
        viewModel.retry()
    }

    func cancel() {
        stateTask?.cancel()
        stateTask = nil

        viewModel.onCleared()
    }
}
