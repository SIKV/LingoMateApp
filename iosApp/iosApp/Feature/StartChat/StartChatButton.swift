import SwiftUI

struct StartChatButton: View {
    var action: () -> Void
    
    @State private var animateGradient = false
    
    var body: some View {
        Button(action: action) {
            HStack {
                Spacer()
                Text(L10n.startChatStartButton)
                Spacer()
                Image(systemName: "sparkles")
            }
            .frame(maxWidth: .infinity)
            .font(.headline)
            .foregroundColor(.white)
            .padding()
            .background(
                LinearGradient(
                    gradient: Gradient(
                        colors: [.blue, .purple]
                    ),
                    startPoint: .topLeading,
                    endPoint: .bottomTrailing
                )
                .hueRotation(.degrees(animateGradient ? 45 : 0))
                .onAppear {
                    withAnimation(.linear(duration: 5).repeatForever(autoreverses: true)) {
                        animateGradient.toggle()
                    }
                }
            )
            .cornerRadius(35)
        }
    }
}
