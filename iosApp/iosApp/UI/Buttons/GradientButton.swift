import SwiftUI

struct GradientButton: View {
    private enum Content {
        case icon(String)
        case title(LocalizedStringKey, fillsWidth: Bool)
    }

    static let height: CGFloat = 38

    private let content: Content
    private let action: () -> Void

    @Environment(\.isEnabled) private var isEnabled

    /// A round button showing only the icon.
    init(systemImage: String, action: @escaping () -> Void) {
        self.content = .icon(systemImage)
        self.action = action
    }

    /// A pill-shaped button showing the title, stretched across the available width if `fillsWidth` is set.
    init(_ title: LocalizedStringKey, fillsWidth: Bool = false, action: @escaping () -> Void) {
        self.content = .title(title, fillsWidth: fillsWidth)
        self.action = action
    }

    var body: some View {
        Button(action: action) {
            label
                .font(.system(size: 15, weight: .semibold))
                .foregroundStyle(isEnabled ? AnyShapeStyle(.white) : AnyShapeStyle(.tertiary))
                // At the minimum width an icon-only button is as wide as it is tall, so the capsule is a circle.
                .frame(minWidth: Self.height, minHeight: Self.height)
                .background {
                    if isEnabled {
                        LinearGradient(
                            colors: [.blue, .purple],
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        )
                    } else {
                        Color(.tertiarySystemFill)
                    }
                }
                .clipShape(Capsule())
        }
        .buttonStyle(.plain)
        .scaleEffect(isEnabled ? 1 : 0.92)
        .animation(.spring(response: 0.3, dampingFraction: 0.7), value: isEnabled)
    }

    @ViewBuilder
    private var label: some View {
        switch content {
        case .icon(let systemImage):
            Image(systemName: systemImage)
        case .title(let title, let fillsWidth):
            Text(title)
                .padding(.horizontal, Spacing.lg)
                .frame(maxWidth: fillsWidth ? .infinity : nil)
        }
    }
}
