import SwiftUI
import Shared

struct ContentView: View {
    var body: some View {
        VStack {
            Text("Hello, Mate!")
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
