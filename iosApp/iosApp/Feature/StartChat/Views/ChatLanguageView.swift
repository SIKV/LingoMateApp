import SwiftUI
import Shared

struct ChatLanguageView: View {
    let languages: [ChatLanguage]
    @Binding var selectedLanguage: ChatLanguage?
    
    var body: some View {
        if languages.isEmpty {
            // FIXME: Show empty state.
        } else {
            Picker(L10n.startChatSelectLanguageLabel, selection: $selectedLanguage) {
                ForEach(languages, id: \.self) { language in
                    Text(language.localizedName)
                        .tag(language)
                }
            }
            .pickerStyle(.menu)
        } 
    }
}
