import SwiftUI
import Shared

struct ContentView: View {
    @StateObject private var appRouter = AppRouter()
    
    var body: some View {
        NavigationStack(path: $appRouter.path) {
            TabView {
                StartChatView()
                    .tabItem {
                        Label(L10n.tabChat, systemImage: "bubble.left.and.bubble.right.fill")
                    }
                HistoryView()
                    .tabItem {
                        Label(L10n.tabHistory, systemImage: "book.pages.fill")
                    }
                MoreView()
                    .tabItem {
                        Label(L10n.tabHistory, systemImage: "ellipsis.circle")
                    }
            }
            .navigationDestination(for: Route.self) { route in
                switch route {
                case .chat:
                    ChatView()
                case .chatDetails:
                    ChatDetailsView()
                }
            }
        }
        .environmentObject(appRouter)
    }
}
