import SwiftUI

struct ChatView: View {
    @EnvironmentObject private var appRouter: AppRouter
    
    var body: some View {
        VStack {
            Text("Chat")
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
    }
}
