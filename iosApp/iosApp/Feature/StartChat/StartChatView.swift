import SwiftUI

struct StartChatView: View {
    @EnvironmentObject private var appRouter: AppRouter
    
    var body: some View {
        GeometryReader { geo in
            if geo.isLandscape {
                VStack {
                    HStack {
                        SectionView("Chat Language") {
                            ChatLanguageView()
                        }
                        .padding()
                        
                        SectionView("Chat Length") {
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
                    SectionView("Chat Language") {
                        ChatLanguageView()
                    }
                    .padding()
                    
                    SectionView("Chat Length") {
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
