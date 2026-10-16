import Combine
import SwiftUI
import Shared
import KMPNativeCoroutinesAsync

@MainActor
class ChatRecapVM: ObservableObject {
    @Published var state = ChatRecapState.companion.empty()

    private var stateTask: Task<Void, Never>?
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

    func retry() {
        viewModel.retry()
    }

    func cancel() {
        stateTask?.cancel()
        stateTask = nil

        viewModel.onCleared()
    }
}
