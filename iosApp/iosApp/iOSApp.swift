import SwiftUI
import Shared

@main
struct iOSApp: App {
    
    init() {
        HelperKt.doInitKoinIOS()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
