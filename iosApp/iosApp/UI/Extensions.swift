import SwiftUI
import Shared

extension GeometryProxy {
    var isLandscape: Bool { size.width > size.height }
}

extension ChatLanguage {
    var localizedName: LocalizedStringKey {
        switch self {
        case .english: return L10n.chatLanguageEnglish
        case .spanish: return L10n.chatLanguageSpanish
        default:
            fatalError("Unknown ChatLanguage value: \(self)")
        }
    }
}
