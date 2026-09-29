import SwiftUI
import Shared

extension GeometryProxy {
    var isLandscape: Bool { size.width > size.height }
}

extension ChatModelProvider {
    var localizedNameString: String {
        switch self {
        case .onDevice: return NSLocalizedString("chat_model_provider_on_device", comment: "")
        case .openAi: return NSLocalizedString("chat_model_provider_open_ai", comment: "")
        default:
            fatalError("Unknown ChatModelProvider value: \(self)")
        }
    }
}

extension Language {
    var localizedName: LocalizedStringKey {
        switch self {
        case .arabic: return L10n.languageArabic
        case .czech: return L10n.languageCzech
        case .danish: return L10n.languageDanish
        case .dutch: return L10n.languageDutch
        case .english: return L10n.languageEnglish
        case .finnish: return L10n.languageFinnish
        case .french: return L10n.languageFrench
        case .german: return L10n.languageGerman
        case .greek: return L10n.languageGreek
        case .hungarian: return L10n.languageHungarian
        case .italian: return L10n.languageItalian
        case .japanese: return L10n.languageJapanese
        case .korean: return L10n.languageKorean
        case .norwegian: return L10n.languageNorwegian
        case .polish: return L10n.languagePolish
        case .portuguese: return L10n.languagePortuguese
        case .romanian: return L10n.languageRomanian
        case .spanish: return L10n.languageSpanish
        case .swedish: return L10n.languageSwedish
        case .turkish: return L10n.languageTurkish
        case .ukrainian: return L10n.languageUkrainian
        default:
            fatalError("Unknown Language value: \(self)")
        }
    }

    var localizedNameWithFlag: Text {
        Text(verbatim: "\(flagEmoji) ") + Text(localizedName)
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

    var localizedDescription: LocalizedStringKey {
        switch self {
        case .conversation: return L10n.practiceTypeConversationDescription
        case .translation: return L10n.practiceTypeTranslationDescription
        default:
            fatalError("Unknown PracticeType value: \(self)")
        }
    }
}

extension ApiKeyProvider {
    // Backs both forms below so the provider is mapped to a string in one place only.
    private var localizedNameKey: String {
        switch self {
        case .openai: return "api_key_provider_open_ai"
        default:
            fatalError("Unknown ApiKeyProvider value: \(self)")
        }
    }

    var localizedName: LocalizedStringKey {
        LocalizedStringKey(localizedNameKey)
    }

    /// Resolved name, for messages that have to be composed at runtime.
    var localizedNameString: String {
        NSLocalizedString(localizedNameKey, comment: "")
    }
}
