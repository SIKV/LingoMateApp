import SwiftUI

struct StartChatButton: View {
    var enabled: Bool = true
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
            .foregroundStyle(enabled ? AnyShapeStyle(.white) : AnyShapeStyle(.tertiary))
            .padding()
            .background {
                if enabled {
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
                } else {
                    Color(.tertiarySystemFill)
                }
            }
            .cornerRadius(35)
        }
        .disabled(!enabled)
    }
}
