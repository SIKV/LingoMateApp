import SwiftUI

extension View {
    func shimmer() -> some View {
        modifier(Shimmer())
    }
}

private struct Shimmer: ViewModifier {
    private let duration: TimeInterval = 1.2

    func body(content: Content) -> some View {
        content
            .overlay {
                TimelineView(.animation) { timeline in
                    let progress = timeline.date.timeIntervalSinceReferenceDate
                        .truncatingRemainder(dividingBy: duration) / duration

                    GeometryReader { geometry in
                        // The band starts fully left of the content and ends fully right of it, so each sweep
                        // enters and leaves.
                        let bandWidth = geometry.size.width / 2

                        LinearGradient(
                            colors: [.clear, .white.opacity(0.3), .clear],
                            startPoint: .leading,
                            endPoint: .trailing
                        )
                        .frame(width: bandWidth)
                        .offset(x: -bandWidth + progress * (geometry.size.width + bandWidth))
                    }
                }
                // Masked by the content, so only the shapes it draws light up, not the gaps between them.
                .mask(content)
            }
    }
}
