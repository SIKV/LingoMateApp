import SwiftUI

struct ChatLanguageView: View {
    // FIXME: Use real data.
    var languages = ["English", "Spanish"]
    
    @State private var selectedLanguage: String = "English"
    
    var body: some View {
        Picker(L10n.startChatSelectLanguageLabel, selection: $selectedLanguage) {
            ForEach(languages, id: \.self) { language in
                Text(language)
            }
        }
        .pickerStyle(.menu)
    }
}
