import Combine
import SwiftUI
import Shared
import KMPNativeCoroutinesAsync

@MainActor
class StartChatModel: ObservableObject {
    private let viewModel = ViewModels().getStartChatViewModel()
    
    @Published var state = StartChatState.companion.empty()
    
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
    
    func selectLanguage(selectedLanguage: ChatLanguage?) {
        if let language = selectedLanguage {
            viewModel.selectLanguage(selectedLanguage: language)
        }
    }
    
    func selectLength(selectedLength: ChatLength?) {
        if let length = selectedLength {
            viewModel.selectLength(selectedLength: length)
        }
    }
    
    func cancel() {
        stateTask?.cancel()
        stateTask = nil
        
        viewModel.onCleared()
    }
}
