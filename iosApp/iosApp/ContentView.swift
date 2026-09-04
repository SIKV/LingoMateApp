import SwiftUI
import Shared

struct ContentView: View {
    @StateObject private var appRouter = AppRouter()
    
    var body: some View {
        NavigationStack(path: $appRouter.path) {
            TabView {
                StartChatScreen()
                    .tabItem {
                        Label(L10n.tabPractice, systemImage: "sparkles")
                    }
                MoreView()
                    .tabItem {
                        Label(L10n.tabMore, systemImage: "ellipsis.circle")
                    }
            }
            .navigationDestination(for: Route.self) { route in
                switch route {
                case .chat(let chatConfig):
                    ChatScreen(chatConfig: chatConfig)
                case .chatDetails:
                    ChatDetailsView()
                case .manageApiKeys:
                    ManageApiKeysScreen()
                }
            }
        }
        .environmentObject(appRouter)
    }
}
