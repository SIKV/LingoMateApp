import SwiftUI

/// Briefly confirms a successful action over the whole app, standing in for the toast iOS lacks.
@MainActor
final class ToastPresenter: ObservableObject {
    @Published private(set) var message: LocalizedStringKey?

    func show(_ message: LocalizedStringKey) {
        self.message = message

        Task {
            try? await Task.sleep(for: .seconds(2))
            self.message = nil
        }
    }
}

extension View {
    func toastOverlay(_ presenter: ToastPresenter) -> some View {
        modifier(ToastOverlay(presenter: presenter))
    }
}

private struct ToastOverlay: ViewModifier {
    @ObservedObject var presenter: ToastPresenter

    func body(content: Content) -> some View {
        content.overlay(alignment: .top) {
            ZStack {
                if let message = presenter.message {
                    ToastView(message: message)
                        .transition(.move(edge: .top).combined(with: .opacity))
                }
            }
            .animation(.spring(response: 0.4, dampingFraction: 0.8), value: presenter.message)
            .sensoryFeedback(.success, trigger: presenter.message) { _, message in
                message != nil
            }
        }
    }
}

private struct ToastView: View {
    let message: LocalizedStringKey

    var body: some View {
        Label {
            Text(message)
        } icon: {
            Image(systemName: "checkmark.circle.fill")
                .foregroundStyle(.green)
        }
        .font(.subheadline.weight(.semibold))
        .padding(.horizontal, Spacing.lg)
        .padding(.vertical, Spacing.md)
        .background(.regularMaterial, in: Capsule())
        .shadow(color: .black.opacity(0.15), radius: 10, y: 4)
    }
}
