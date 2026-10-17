import SwiftUI

struct MoreView: View {
    var body: some View {
        List {
            NavigationLink(value: Route.manageApiKeys) {
                MoreRow(
                    title: L10n.moreManageApiKeysTitle,
                    subtitle: L10n.moreManageApiKeysSubtitle,
                    systemImage: "key.fill"
                )
            }

            NavigationLink(value: Route.feedback) {
                MoreRow(
                    title: L10n.moreFeedbackTitle,
                    subtitle: L10n.moreFeedbackSubtitle,
                    systemImage: "exclamationmark.bubble.fill"
                )
            }
        }
        .safeAreaInset(edge: .bottom) {
            Text(L10n.moreAppVersion(appVersion))
                .font(.caption2)
                .foregroundStyle(.secondary)
                .padding(.bottom, Spacing.sm)
        }
        .navigationTitle(L10n.moreTitle)
    }

    private var appVersion: String {
        Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String ?? ""
    }
}

private struct MoreRow: View {
    let title: LocalizedStringKey
    let subtitle: LocalizedStringKey
    let systemImage: String

    var body: some View {
        Label {
            VStack(alignment: .leading, spacing: Spacing.xs) {
                Text(title)

                Text(subtitle)
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
            }
        } icon: {
            Image(systemName: systemImage)
                .foregroundStyle(Color.accentColor)
        }
    }
}
