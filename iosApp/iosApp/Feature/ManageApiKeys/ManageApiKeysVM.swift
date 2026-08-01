import Combine
import SwiftUI
import Shared
import KMPNativeCoroutinesAsync

@MainActor
class ManageApiKeysVM: ObservableObject {
    private let viewModel = ViewModels().getManageApiKeysViewModel()

    @Published var state = ManageApiKeysState.companion.empty()

    private var stateTask: Task<Void, Never>?

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
