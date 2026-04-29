import SwiftUI
import Shared
import KMPNativeCoroutinesAsync

struct StartChatScreen: View {
    @EnvironmentObject private var appRouter: AppRouter
    @StateObject private var startChatVM = StartChatVM()
    
    var body: some View {
        GeometryReader { geo in
            let widthMultiplier = geo.isLandscape ? 0.65 : 0.85
            
            ZStack {
                LinearGradient(
                    colors: [
                        Color(.systemBackground),
                        Color(.secondarySystemBackground)
                    ],
                    startPoint: .top,
                    endPoint: .bottom
                )
                .ignoresSafeArea()
                
                VStack() {
                    VStack(spacing: Spacing.md) {
                        if !geo.isLandscape {
                            ZStack {
                                Circle()
                                    .fill(Color.accentColor.opacity(0.12))
                                    .frame(width: 72, height: 72)
                                Image(systemName: "bubble.left.and.bubble.right.fill")
                                    .foregroundStyle(Color.accentColor)
                                    .font(.system(size: 28, weight: .semibold))
                            }
                        }
                        
                        Text(L10n.startChatGreeting)
                            .font(.system(size: 28, weight: .bold, design: .rounded))
                            .foregroundStyle(.primary)
                            .padding(.top, Spacing.md)
                        
                        Text(L10n.startChatInfo)
                            .font(.subheadline)
                            .foregroundStyle(.secondary)
                            .multilineTextAlignment(.center)
                            .padding(.top, Spacing.sm)
                    }
                    
                    languageCard
                        .frame(width: geo.size.width * widthMultiplier)
                    
                    StartChatButton {
                        guard let chatLanguage = startChatVM.state.selectedLanguage else { return }
                        appRouter.navigate(to: Route.chat(chatLanguage))
                    }
                    .padding(.vertical, Spacing.md)
                    .frame(width: geo.size.width * widthMultiplier)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
            }
        }
        .task {
            startChatVM.listenState()
        }
        .onDisappear {
            startChatVM.cancel()
        }
    }
    
    private var languageCard: some View {
        VStack(spacing: Spacing.md) {
            VStack(spacing: Spacing.sm) {
                ChatLanguageView(
                    languages: startChatVM.state.chatLanguages,
                    selectedLanguage: Binding(
                        get: { startChatVM.state.selectedLanguage },
                        set: { startChatVM.selectLanguage(selectedLanguage: $0) }
                    )
                )
                .frame(maxWidth: .infinity)
            }
            .padding(.vertical, Spacing.sm)
            .padding(.horizontal, Spacing.lg)
            .background(.ultraThinMaterial, in: RoundedRectangle(cornerRadius: Radius.xxl, style: .continuous))
            .overlay(
                RoundedRectangle(cornerRadius: Radius.xxl, style: .continuous)
                    .stroke(Color.gray.opacity(0.12), lineWidth: 1)
            )
            .shadow(color: Color.black.opacity(0.03), radius: 10, x: 0, y: 6)
        }
        .padding(.top, Spacing.sm)
        .frame(maxWidth: .infinity, alignment: .center)
    }
}
