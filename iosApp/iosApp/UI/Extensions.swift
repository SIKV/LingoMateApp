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

extension ChatLength {
    var localizedName: LocalizedStringKey {
        switch self {
        case .shortLength: return L10n.chatLengthShort
        case .mediumLength: return L10n.chatLengthMedium
        case .longLength: return L10n.chatLengthLong
        default:
            fatalError("Unknown ChatLanguage value: \(self)")
        }
    }
}
