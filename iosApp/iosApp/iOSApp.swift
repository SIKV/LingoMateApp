import SwiftUI
import Shared

@main
struct iOSApp: App {
    
    init() {
        OnDeviceLLM_iosKt.setIOSOnDeviceLLMProvider(protocol: IOSOnDeviceLLMProvider())
        HelperKt.doInitKoinIOS()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

