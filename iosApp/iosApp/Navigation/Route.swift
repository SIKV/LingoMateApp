import SwiftUI
import Shared

enum Route: Hashable {
    case chat(ChatConfig)
    case chatRecap(ChatConfig, [ChatMessage])
    case chatDetails
    case manageApiKeys
}
