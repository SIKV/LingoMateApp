import SwiftUI
import Shared

struct ChatScreen: View {
    let chatLanguage: ChatLanguage
    let chatLength: ChatLength
    
    @EnvironmentObject private var appRouter: AppRouter
    @StateObject private var chatVM: ChatVM
    
    init(chatLanguage: ChatLanguage, chatLength: ChatLength) {
        self.chatLanguage = chatLanguage
        self.chatLength = chatLength
        
        _chatVM = StateObject(wrappedValue: ChatVM(
            chatLanguage: chatLanguage,
            chatLength: chatLength
        ))
    }
    
    var body: some View {
        VStack {
            Text("Chat")
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
    }
}
