import Combine
import SwiftUI
import Shared
import KMPNativeCoroutinesAsync

@MainActor
class ManageApiKeysVM: ObservableObject {
    private let viewModel = ViewModels().getManageApiKeysViewModel()

    @Published var state = ManageApiKeysState.companion.empty()

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

    func addApiKey(_ provider: ApiKeyProvider, apiKey: String) {
        viewModel.addApiKey(provider: provider, apiKey: apiKey)
    }

    func removeApiKey(_ provider: ApiKeyProvider) {
        viewModel.removeApiKey(provider: provider)
    }

    func cancel() {
        stateTask?.cancel()
        stateTask = nil

        viewModel.onCleared()
    }
}
