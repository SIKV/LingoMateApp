import SwiftUI
import Shared

struct ChatScreen: View {
    let chatLanguage: ChatLanguage
    let chatLength: ChatLength
    
    @EnvironmentObject private var appRouter: AppRouter
    @StateObject private var chatVM: ChatVM
    
    // Local input state.
    @State private var inputText: String = ""
    // Disable send logic while any message (user or assistant) is in progress.
    private var isSending: Bool {
        chatVM.state.messages.contains { $0.status == ChatMessage.Status.inProgress }
    }
    
    init(chatLanguage: ChatLanguage, chatLength: ChatLength) {
        self.chatLanguage = chatLanguage
        self.chatLength = chatLength
        
        _chatVM = StateObject(wrappedValue: ChatVM(
            chatLanguage: chatLanguage,
            chatLength: chatLength
        ))
    }
    
    var body: some View {
        GeometryReader { geo in
            ScrollViewReader { proxy in
                ScrollView {
                    LazyVStack(spacing: Spacing.md) {
                        ForEach(chatVM.state.messages, id: \.id) { message in
                            MessageRow(
                                message: message,
                                maxBubbleWidth: geo.size.width * 0.75
                            )
                            .id(message.id)
                            .padding(.horizontal, Spacing.md)
                        }
                    }
                    .padding(.vertical, Spacing.md)
                }
                .onChange(of: chatVM.state.messages.count) {
                    if let last = chatVM.state.messages.last {
                        withAnimation {
                            proxy.scrollTo(last.id, anchor: .bottom)
                        }
                    }
                }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
        .task {
            chatVM.listenState()
        }
        .onDisappear {
            chatVM.cancel()
        }
        .navigationBarTitleDisplayMode(.inline)
        .safeAreaInset(edge: .bottom) {
            inputBar
                .background(.ultraThinMaterial)
                .overlay(Divider(), alignment: .top)
        }
        .ignoresSafeArea(.keyboard, edges: .bottom)
    }
    
    private var inputBar: some View {
        HStack(spacing: Spacing.sm) {
            TextField(L10n.chatTypeMessageHint, text: $inputText, axis: .vertical)
                .textFieldStyle(.roundedBorder)
                .lineLimit(1...5)
                .submitLabel(.send)
                .onSubmit {
                    send()
                }
            
            Button {
                send()
            } label: {
                Image(systemName: "paperplane.fill")
                    .font(.system(size: 17, weight: .semibold))
            }
            // Enabled only when there is non-whitespace text and not currently sending.
            .disabled(inputText.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty || isSending)
            .buttonStyle(.borderedProminent)
        }
        .padding(.horizontal, Spacing.md)
        .padding(.vertical, Spacing.sm)
    }
    
    private func send() {
        let text = inputText.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !text.isEmpty else { return }
        
        chatVM.send(text)
        inputText = ""
    }
}

private struct MessageRow: View {
    let message: ChatMessage
    let maxBubbleWidth: CGFloat
    
    @State private var animateTyping = false
    
    var isUser: Bool {
        message.role == ChatMessage.Role.user
    }
    
    var bubbleColor: Color {
        isUser ? Color.accentColor : Color(.secondarySystemBackground)
    }
    
    var textColor: Color {
        isUser ? .white : .primary
    }
    
    private var isAssistantTypingPlaceholder: Bool {
        message.status == ChatMessage.Status.inProgress && !isUser
    }
    
    var body: some View {
        Group {
            if isAssistantTypingPlaceholder {
                assistantTypingBubble
                    .frame(maxWidth: maxBubbleWidth, alignment: .leading)
            } else {
                bubbleContent
                    .frame(maxWidth: maxBubbleWidth, alignment: isUser ? .trailing : .leading)
            }
        }
        // Full-width row; align bubble to trailing for user, leading for others.
        .frame(maxWidth: .infinity, alignment: isUser ? .trailing : .leading)
        .onAppear {
            if isAssistantTypingPlaceholder {
                animateTyping = true
            }
        }
        .onDisappear {
            animateTyping = false
        }
    }
    
    private var bubbleContent: some View {
        VStack(alignment: .trailing, spacing: Spacing.sm) {
            Text(message.text)
                .foregroundStyle(textColor)
                .font(.body)
                .multilineTextAlignment(.leading)
                .padding(.vertical, Spacing.sm)
                .padding(.horizontal, Spacing.md) // Internal bubble padding only.
                .background(bubbleColor, in: RoundedRectangle(cornerRadius: Radius.lg, style: .continuous))
                .overlay(
                    RoundedRectangle(cornerRadius: Radius.lg, style: .continuous)
                        .stroke(isUser ? Color.clear : Color.gray.opacity(0.15), lineWidth: 1)
                )
            
            HStack {
                // This is shown only for user messages.
                if message.status == ChatMessage.Status.inProgress {
                    ProgressView()
                        .scaleEffect(0.8)
                } else if message.status == ChatMessage.Status.failed {
                    Image(systemName: "exclamationmark.triangle.fill")
                        .foregroundStyle(.yellow)
                        .font(.caption)
                }
            }
        }
    }
    
    private var assistantTypingBubble: some View {
        HStack(spacing: Spacing.sm) {
            ForEach(0..<3) { index in
                Circle()
                    .fill(.secondary)
                    .frame(width: 6, height: 6)
                    .opacity(0.6)
                    .scaleEffect(typingScale(for: index))
                    .animation(
                        .easeInOut(duration: 0.8)
                            .repeatForever()
                            .delay(0.15 * Double(index)),
                        value: animateTyping
                    )
            }
        }
        .padding(.vertical, Spacing.md)
        .padding(.horizontal, Spacing.lg)
        .background(bubbleColor, in: RoundedRectangle(cornerRadius: Radius.lg, style: .continuous))
        .overlay(
            RoundedRectangle(cornerRadius: Radius.lg, style: .continuous)
                .stroke(Color.gray.opacity(0.15), lineWidth: 1)
        )
    }
    
    private func typingScale(for index: Int) -> CGFloat {
        // Pulse all three dots; per-dot delay creates a wave effect.
        if animateTyping {
            return 1.15
        } else {
            return 0.85
        }
    }
}
