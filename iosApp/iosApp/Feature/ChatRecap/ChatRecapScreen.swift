import SwiftUI
import Shared

struct ChatRecapScreen: View {
    @EnvironmentObject private var appRouter: AppRouter
    @StateObject private var chatRecapVM: ChatRecapVM

    init(chatConfig: ChatConfig, messages: [ChatMessage]) {
        _chatRecapVM = StateObject(wrappedValue: ChatRecapVM(
            chatConfig: chatConfig,
            messages: messages
        ))
    }

    var body: some View {
        Group {
            if let recap = chatRecapVM.state.recap {
                recapView(recap)
            } else if chatRecapVM.state.hasFailed {
                failedState
            } else {
                loadingState
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .navigationBarTitleDisplayMode(.inline)
        .safeAreaInset(edge: .bottom) {
            doneButton
        }
        .task {
            chatRecapVM.listenState()
        }
        .onDisappear {
            chatRecapVM.cancel()
        }
    }

    // MARK: - Recap

    private func recapView(_ recap: ChatRecap) -> some View {
        Text(recap.summary)
            .multilineTextAlignment(.center)
            .padding(.horizontal, Spacing.xl)
    }

    // MARK: - Loading and failure

    // Placeholders in the shape of the recap: a few lines of summary.
    private var loadingState: some View {
        VStack(spacing: Spacing.sm) {
            ForEach([0, Spacing.lg, Spacing.xl * 3], id: \.self) { inset in
                Capsule()
                    .frame(height: 14)
                    .padding(.horizontal, inset)
            }
        }
        .foregroundStyle(Color(.systemGray5))
        .shimmer()
        .padding(.horizontal, Spacing.xl)
        .accessibilityElement()
        .accessibilityLabel(L10n.chatRecapLoading)
    }

    private var failedState: some View {
        ContentUnavailableView {
            Label(L10n.chatRecapFailed, systemImage: "exclamationmark.bubble")
        } actions: {
            Button(L10n.chatRecapRetryButton) {
                chatRecapVM.retry()
            }
        }
    }

    private var doneButton: some View {
        GradientButton(L10n.chatRecapDoneButton, fillsWidth: true) {
            appRouter.navigateBack()
        }
        .padding(.horizontal, Spacing.lg)
        .padding(.vertical, Spacing.sm)
    }
}
