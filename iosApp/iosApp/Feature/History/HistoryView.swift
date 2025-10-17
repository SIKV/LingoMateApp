import SwiftUI

struct HistoryView: View {
    @EnvironmentObject private var appRouter: AppRouter
    
    var body: some View {
        VStack {
            Text("History")
            Button("Go to Chat Details") {
                appRouter.navigate(to: Route.chatDetails)
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
    }
}
