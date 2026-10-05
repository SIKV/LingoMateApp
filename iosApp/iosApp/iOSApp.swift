import SwiftUI
import Shared

@main
struct iOSApp: App {
    
    init() {
        OnDeviceLLM_iosKt.setIOSOnDeviceLLMProvider(protocol: IOSOnDeviceLLMProvider())
        HelperKt.doInitKoinIOS()
        #if DEBUG
        SentryKt.doInitSentry(isDebug: true)
        #else
        SentryKt.doInitSentry(isDebug: false)
        #endif
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
