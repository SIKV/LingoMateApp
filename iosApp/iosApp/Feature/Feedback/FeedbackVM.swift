import Combine
import SwiftUI
import Shared
import KMPNativeCoroutinesAsync

@MainActor
class FeedbackVM: ObservableObject {
    private let viewModel = ViewModels().getFeedbackViewModel()

    @Published var state = FeedbackState.companion.empty()

    // uiState is a StateFlow, so the only error is CancellationError from cancel(), which needs no handling.
    private var stateTask: Task<Void, Error>?

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

    func send(message: String, email: String) {
        viewModel.send(message: message, email: email)
    }

    func cancel() {
        stateTask?.cancel()
        stateTask = nil

        viewModel.onCleared()
    }
}
