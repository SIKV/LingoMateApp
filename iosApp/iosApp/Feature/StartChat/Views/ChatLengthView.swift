import SwiftUI
import Shared

struct ChatLengthView: View {
    let lengths: [ChatLength]
    @Binding var selectedLength: ChatLength?
    
    var body: some View {
        if lengths.isEmpty {
            // FIXME: Show empty state.
        } else {
            Picker(L10n.startChatSelectLengthLabel, selection: $selectedLength) {
                ForEach(lengths, id: \.self) { length in
                    Text(length.localizedName)
                        .tag(length)
                }
            }
            .pickerStyle(.segmented)
        }
    }
}
