import SwiftUI
import Shared
import KMPNativeCoroutinesAsync

struct StartChatScreen: View {
    @EnvironmentObject private var appRouter: AppRouter
    @StateObject private var startChatVM = StartChatVM()
    
    var body: some View {
        GeometryReader { geo in
            if geo.isLandscape {
                VStack {
                    HStack {
                        SectionView(L10n.startChatSelectLanguageLabel) {
                            ChatLanguageView(
                                languages: startChatVM.state.chatLanguages,
                                selectedLanguage: Binding(
                                    get: { startChatVM.state.selectedLanguage },
                                    set: { startChatVM.selectLanguage(selectedLanguage: $0) }
                                )
                            )
                        }
                        .padding()
                        
                        SectionView(L10n.startChatSelectLengthLabel) {
                            ChatLengthView(
                                lengths: startChatVM.state.chatLengths,
                                selectedLength: Binding(
                                    get: { startChatVM.state.selectedLength },
                                    set: { startChatVM.selectLength(selectedLength: $0) }
                                )
                            )
                        }
                        .padding()
                    }
                    
                    Spacer()
                    
                    StartChatButton {
                        if let chatLanguage = startChatVM.state.selectedLanguage, let chatLength = startChatVM.state.selectedLength {
                            appRouter.navigate(to: Route.chat(chatLanguage, chatLength))
                        }
                    }
                    .padding()
                }
            } else {
                VStack {
                    SectionView(L10n.startChatSelectLanguageLabel) {
                        ChatLanguageView(
                            languages: startChatVM.state.chatLanguages,
                            selectedLanguage: Binding(
                                get: { startChatVM.state.selectedLanguage },
                                set: { startChatVM.selectLanguage(selectedLanguage: $0) }
                            )
                        )
                    }
                    .padding()
                    
                    SectionView(L10n.startChatSelectLengthLabel) {
                        ChatLengthView(
                            lengths: startChatVM.state.chatLengths,
                            selectedLength: Binding(
                                get: { startChatVM.state.selectedLength },
                                set: { startChatVM.selectLength(selectedLength: $0) }
                            )
                        )
                    }
                    .padding()
                    
                    Spacer()
                    
                    StartChatButton {
                        if let chatLanguage = startChatVM.state.selectedLanguage, let chatLength = startChatVM.state.selectedLength {
                            appRouter.navigate(to: Route.chat(chatLanguage, chatLength))
                        }
                    }
                    .padding()
                }
                .padding()
            }
        }
        .task {
            startChatVM.listenState()
        }
        .onDisappear {
            startChatVM.cancel()
        }
    }
}
