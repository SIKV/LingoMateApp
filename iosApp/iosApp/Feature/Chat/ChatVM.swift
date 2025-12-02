import Combine
import SwiftUI
import Shared

@MainActor
class ChatVM: ObservableObject {
    let chatLanguage: ChatLanguage
    let chatLength: ChatLength
    
    private let viewModel: ChatViewModel
    
    init(chatLanguage: ChatLanguage, chatLength: ChatLength) {
        self.chatLanguage = chatLanguage
        self.chatLength = chatLength
        
        viewModel = ViewModels().getChatViewModel(
            chatLanguage: chatLanguage,
            chatLength: chatLength,
            chatModel: ChatModel.gpt5Nano // FIXME: Hardcoded for now.
        )
    }
}
