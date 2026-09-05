import SwiftUI

struct StartChatButton: View {
    var enabled: Bool = true
    var action: () -> Void

    @State private var animateGradient = false

    var body: some View {
        Button(action: action) {
            HStack(spacing: Spacing.sm) {
                Image(systemName: "sparkles")
                Text(L10n.startChatStartButton)
            }
            .font(.system(size: 19, weight: .semibold, design: .rounded))
            .foregroundStyle(enabled ? AnyShapeStyle(.white) : AnyShapeStyle(.tertiary))
            .frame(maxWidth: .infinity, minHeight: 60)
            .background {
                if enabled {
                    LinearGradient(
                        colors: [.blue, .purple],
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
            .clipShape(Capsule(style: .continuous))
            // A brighter top edge fading downwards reads as light falling on the pill.
            .overlay {
                Capsule(style: .continuous)
                    .strokeBorder(
                        LinearGradient(
                            colors: [.white.opacity(0.45), .white.opacity(0.05)],
                            startPoint: .top,
                            endPoint: .bottom
                        ),
                        lineWidth: 1
                    )
                    .opacity(enabled ? 1 : 0)
            }
            .shadow(color: enabled ? .purple.opacity(0.35) : .clear, radius: 16, y: 8)
        }
        .buttonStyle(PressScaleButtonStyle())
        .disabled(!enabled)
        .animation(.easeInOut(duration: 0.2), value: enabled)
    }
}

/// Presses sink the button slightly instead of only dimming it.
private struct PressScaleButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? 0.97 : 1)
            .animation(.spring(response: 0.3, dampingFraction: 0.6), value: configuration.isPressed)
    }
}
