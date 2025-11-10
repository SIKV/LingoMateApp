import SwiftUI

struct StartChatView: View {
    @EnvironmentObject private var appRouter: AppRouter
    
    var body: some View {
        GeometryReader { geo in
            if geo.isLandscape {
                VStack {
                    HStack {
                        SectionView(L10n.startChatSelectLanguageLabel) {
                            ChatLanguageView()
                        }
                        .padding()
                        
                        SectionView(L10n.startChatSelectLengthLabel) {
                            ChatLengthView()
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
                        ChatLanguageView()
                    }
                    .padding()
                    
                    SectionView(L10n.startChatSelectLengthLabel) {
                        ChatLengthView()
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
    }
}
