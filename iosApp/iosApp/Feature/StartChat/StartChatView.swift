import SwiftUI
import Shared
import KMPNativeCoroutinesAsync

struct StartChatView: View {
    @EnvironmentObject private var appRouter: AppRouter
    @StateObject private var startChatModel = StartChatModel()
    
    var body: some View {
        GeometryReader { geo in
            if geo.isLandscape {
                VStack {
                    HStack {
                        SectionView(L10n.startChatSelectLanguageLabel) {
                            ChatLanguageView(
                                languages: startChatModel.state.chatLanguages,
                                selectedLanguage: Binding(
                                    get: { startChatModel.state.selectedLanguage },
                                    set: { startChatModel.selectLanguage(selectedLanguage: $0) }
                                )
                            )
                        }
                        .padding()
                        
                        SectionView(L10n.startChatSelectLengthLabel) {
                            ChatLengthView(
                                lengths: startChatModel.state.chatLengths,
                                selectedLength: Binding(
                                    get: { startChatModel.state.selectedLength },
                                    set: { startChatModel.selectLength(selectedLength: $0) }
                                )
                            )
                        }
                        .padding()
                    }
                    
                    Spacer()
                    
                    StartChatButton {
                        appRouter.navigate(to: Route.chat)
                    }
                    .padding()
                }
            } else {
                VStack {
                    SectionView(L10n.startChatSelectLanguageLabel) {
                        ChatLanguageView(
                            languages: startChatModel.state.chatLanguages,
                            selectedLanguage: Binding(
                                get: { startChatModel.state.selectedLanguage },
                                set: { startChatModel.selectLanguage(selectedLanguage: $0) }
                            )
                        )
                    }
                    .padding()
                    
                    SectionView(L10n.startChatSelectLengthLabel) {
                        ChatLengthView(
                            lengths: startChatModel.state.chatLengths,
                            selectedLength: Binding(
                                get: { startChatModel.state.selectedLength },
                                set: { startChatModel.selectLength(selectedLength: $0) }
                            )
                        )
                    }
                    .padding()
                    
                    Spacer()
                    
                    StartChatButton {
                        appRouter.navigate(to: Route.chat)
                    }
                    .padding()
                }
                .padding()
            }
        }
        .task {
            startChatModel.listenState()
        }
        .onDisappear {
            startChatModel.cancel()
        }
    }
}
