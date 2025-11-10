import SwiftUI

struct ChatLengthView: View {
    // FIXME: Use real data.
    @State private var lengths = ["Short", "Medium", "Long"]
    @State private var selectedLength = "Medium"
    
    var body: some View {
        Picker(L10n.startChatSelectLengthLabel, selection: $selectedLength) {
            ForEach(lengths, id: \.self) {
                Text($0)
            }
        }
        .pickerStyle(.segmented)
    }
}
