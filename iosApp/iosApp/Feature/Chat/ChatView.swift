import SwiftUI

struct ChatView: View {
    @EnvironmentObject private var appRouter: AppRouter
    
    var body: some View {
        VStack {
            Text("Chat")
            Button("Go to Chat Details") {
                appRouter.navigate(to: Route.chatDetails)
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
    }
}
