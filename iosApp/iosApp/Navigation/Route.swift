import SwiftUI
import Shared

enum Route: Hashable {
    case chat(ChatLanguage, ChatLength)
    case chatDetails
}
