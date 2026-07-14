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

                VStack(spacing: Spacing.xl) {
                    header(showIcon: !geo.isLandscape)

                    configCard
                        .frame(width: geo.size.width * widthMultiplier)

                    StartChatButton(enabled: startChatVM.chatConfig != nil) {
                        guard let chatConfig = startChatVM.chatConfig else { return }
                        appRouter.navigate(to: Route.chat(chatConfig))
                    }
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

    // MARK: - Header

    private func header(showIcon: Bool) -> some View {
        VStack(spacing: Spacing.md) {
            if showIcon {
                ZStack {
                    Circle()
                        .fill(Color.accentColor.opacity(0.12))
                        .frame(width: 72, height: 72)
                    Image(systemName: "sparkles")
                        .foregroundStyle(Color.accentColor)
                        .font(.system(size: 28, weight: .semibold))
                }
            }

            Text(L10n.startChatGreeting)
                .font(.system(size: 28, weight: .bold, design: .rounded))
                .foregroundStyle(.primary)

            Text(L10n.startChatInfo)
                .font(.subheadline)
                .foregroundStyle(.secondary)
                .multilineTextAlignment(.center)
        }
    }

    // MARK: - Config card

    private var configCard: some View {
        VStack(spacing: 0) {
            SelectorRow(
                label: L10n.startChatChatModelLabel,
                options: startChatVM.state.chatModels,
                selectedLabel: startChatVM.state.selectedChatModel?.localizedName,
                optionLabel: { $0.localizedName },
                onSelect: startChatVM.selectChatModel
            )

            divider

            SelectorRow(
                label: L10n.startChatPracticeLanguageLabel,
                options: startChatVM.state.practiceLanguages,
                selectedLabel: startChatVM.state.selectedPracticeLanguage?.localizedName,
                optionLabel: { $0.localizedName },
                onSelect: startChatVM.selectPracticeLanguage
            )

            divider

            SelectorRow(
                label: L10n.startChatTranslationLanguageLabel,
                options: startChatVM.state.translationLanguages,
                selectedLabel: startChatVM.state.selectedTranslationLanguage?.localizedName,
                optionLabel: { $0.localizedName },
                onSelect: startChatVM.selectTranslationLanguage
            )

            divider

            SelectorRow(
                label: L10n.startChatPracticeTypeLabel,
                options: startChatVM.state.practiceTypes,
                selectedLabel: startChatVM.state.selectedPracticeType?.localizedName,
                optionLabel: { $0.localizedName },
                onSelect: startChatVM.selectPracticeType
            )
        }
        .background(
            RoundedRectangle(cornerRadius: Radius.xl, style: .continuous)
                .fill(Color(.secondarySystemBackground))
        )
        .overlay(
            RoundedRectangle(cornerRadius: Radius.xl, style: .continuous)
                .stroke(Color.gray.opacity(0.15), lineWidth: 1)
        )
    }

    private var divider: some View {
        Divider()
            .padding(.horizontal, Spacing.lg)
    }
}

// MARK: - Selector row

private struct SelectorRow<Option>: View {
    let label: LocalizedStringKey
    let options: [Option]
    let selectedLabel: LocalizedStringKey?
    let optionLabel: (Option) -> LocalizedStringKey
    let onSelect: (Option) -> Void

    var body: some View {
        Menu {
            ForEach(Array(options.enumerated()), id: \.offset) { _, option in
                Button {
                    onSelect(option)
                } label: {
                    Text(optionLabel(option))
                }
            }
        } label: {
            HStack(alignment: .center) {
                VStack(alignment: .leading, spacing: Spacing.xs) {
                    Text(label)
                        .font(.subheadline)
                        .foregroundStyle(.secondary)

                    Text(selectedLabel ?? L10n.startChatNotSelected)
                        .font(.title3.weight(.medium))
                        .foregroundStyle(selectedLabel == nil ? .secondary : .primary)
                }

                Spacer()

                Image(systemName: "chevron.down")
                    .font(.subheadline.weight(.semibold))
                    .foregroundStyle(.secondary)
            }
            .padding(.horizontal, Spacing.lg)
            .padding(.vertical, Spacing.md)
            .contentShape(Rectangle())
        }
        .disabled(options.isEmpty)
    }
}
