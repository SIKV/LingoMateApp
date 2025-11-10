import SwiftUI

struct ChatLengthView: View {
    // FIXME:
    @State private var lengths = ["Short", "Medium", "Long"]
    @State private var selectedLength = "Medium"
    
    var body: some View {
        Picker("Select Length", selection: $selectedLength) {
            ForEach(lengths, id: \.self) {
                Text($0)
            }
        }
        .pickerStyle(.segmented)
    }
}
