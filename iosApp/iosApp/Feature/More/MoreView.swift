import SwiftUI

struct MoreView: View {
    var body: some View {
        List {
            NavigationLink(value: Route.manageApiKeys) {
                Label {
                    VStack(alignment: .leading, spacing: Spacing.xs) {
                        Text(L10n.moreManageApiKeysTitle)

                        Text(L10n.moreManageApiKeysSubtitle)
                            .font(.subheadline)
                            .foregroundStyle(.secondary)
                    }
                } icon: {
                    Image(systemName: "key.fill")
                        .foregroundStyle(Color.accentColor)
                }
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
