import SwiftUI
import Shared

struct FeedbackScreen: View {
    private static let maxMessageLength = Int(FeedbackViewModel.companion.MAX_MESSAGE_LENGTH)
    private static let maxEmailLength = Int(FeedbackViewModel.companion.MAX_EMAIL_LENGTH)

    @EnvironmentObject private var appRouter: AppRouter
    @EnvironmentObject private var toastPresenter: ToastPresenter
    @StateObject private var feedbackVM = FeedbackVM()

    @State private var message = ""
    @State private var email = ""

    private var canSend: Bool {
        !message.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
    }

    var body: some View {
        Form {
            Section {
                TextField(L10n.feedbackMessageLabel, text: $message, axis: .vertical)
                    .lineLimit(6...)
            } header: {
                Text(L10n.feedbackInfo)
                    // Headers are uppercased by default, which suits a label but not a sentence.
                    .textCase(nil)
            } footer: {
                Text(L10n.feedbackMessageCounter(message.count, Self.maxMessageLength))
                    .monospacedDigit()
                    .frame(maxWidth: .infinity, alignment: .trailing)
            }

            Section {
                TextField(L10n.feedbackEmailLabel, text: $email)
                    .keyboardType(.emailAddress)
                    .textContentType(.emailAddress)
                    .textInputAutocapitalization(.never)
                    .autocorrectionDisabled()
            } footer: {
                Text(L10n.feedbackEmailFooter)
            }
        }
        .navigationTitle(L10n.feedbackTitle)
        .navigationBarTitleDisplayMode(.inline)
        .safeAreaInset(edge: .bottom) {
            sendButton
        }
        .onChange(of: message) {
            if message.count > Self.maxMessageLength {
                message = String(message.prefix(Self.maxMessageLength))
            }
        }
        .onChange(of: email) {
            if email.count > Self.maxEmailLength {
                email = String(email.prefix(Self.maxEmailLength))
            }
        }
        .onChange(of: feedbackVM.state.isSent) {
            if feedbackVM.state.isSent {
                toastPresenter.show(L10n.feedbackSent)
                appRouter.navigateBack()
            }
        }
        .task {
            feedbackVM.listenState()
        }
        .onDisappear {
            feedbackVM.cancel()
        }
    }

    private var sendButton: some View {
        GradientButton(L10n.feedbackSendButton, fillsWidth: true) {
            feedbackVM.send(message: message, email: email)
        }
        .disabled(!canSend)
        .padding(.horizontal, Spacing.lg)
        .padding(.vertical, Spacing.sm)
    }
}
