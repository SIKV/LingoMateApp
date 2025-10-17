import SwiftUI
import Shared

struct ContentView: View {
    @StateObject private var appRouter = AppRouter()
    
    var body: some View {
        NavigationStack(path: $appRouter.path) {
            TabView {
                ChatView()
                    .tabItem {
                        Label("Chat", systemImage: "bubble.left.and.bubble.right.fill")
                    }
                HistoryView()
                    .tabItem {
                        Label("History", systemImage: "book.pages.fill")
                    }
                MoreView()
                    .tabItem {
                        Label("More", systemImage: "ellipsis.circle")
                    }
            }
            .navigationDestination(for: Route.self) { route in
                switch route {
                case .chatDetails:
                    ChatDetailsView()
                }
            }
        }
        .environmentObject(appRouter)
    }
}
