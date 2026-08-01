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

extension AssistantLanguage {
    var localizedName: LocalizedStringKey {
        switch self {
        case .english: return L10n.assistantLanguageEnglish
        default:
            fatalError("Unknown AssistantLanguage value: \(self)")
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
