import SwiftUI
import Shared

extension GeometryProxy {
    var isLandscape: Bool { size.width > size.height }
}

extension ChatModel {
    var localizedName: LocalizedStringKey {
        switch provider {
        case .onDevice: return L10n.chatModelProviderOnDevice
        default: return LocalizedStringKey(model)
        }
    }
}

extension PracticeLanguage {
    var localizedName: LocalizedStringKey {
        switch self {
        case .english: return L10n.chatLanguageEnglish
        case .spanish: return L10n.chatLanguageSpanish
        default:
            fatalError("Unknown PracticeLanguage value: \(self)")
        }
    }
}

extension TranslationLanguage {
    var localizedName: LocalizedStringKey {
        switch self {
        case .english: return L10n.translationLanguageEnglish
        default:
            fatalError("Unknown TranslationLanguage value: \(self)")
        }
    }
}

extension PracticeType {
    var localizedName: LocalizedStringKey {
        switch self {
        case .conversation: return L10n.practiceTypeConversation
        case .translation: return L10n.practiceTypeTranslation
        default:
            fatalError("Unknown PracticeType value: \(self)")
        }
    }
}
