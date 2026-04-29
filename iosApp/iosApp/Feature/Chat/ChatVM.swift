import Combine
import SwiftUI
import Shared
import KMPNativeCoroutinesAsync

@MainActor
class ChatVM: ObservableObject {
    let chatLanguage: ChatLanguage
    
    @Published var state = ChatState.companion.empty()
    
    private var stateTask: Task<Void, Never>?
    private let viewModel: ChatViewModel
    
    init(chatLanguage: ChatLanguage) {
        self.chatLanguage = chatLanguage
        
        viewModel = ViewModels().getChatViewModel(
            chatLanguage: chatLanguage,
            chatModel: ChatModel.gpt51 // FIXME: Hardcoded for now.
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
    
    func send(_ text: String) {
        guard !text.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else { return }
        viewModel.sendMessage(message: text)
    }
    
    func cancel() {
        stateTask?.cancel()
        stateTask = nil
        
        viewModel.onCleared()
    }
}
