import SwiftUI
import Shared

@main
struct iOSApp: App {
    
    init() {
        #if DEBUG
        let isDebug = true
        #else
        let isDebug = false
        #endif

        OnDeviceLLM_iosKt.setIOSOnDeviceLLMProvider(protocol: IOSOnDeviceLLMProvider())

        HelperKt.doInitKoinIOS(
            analytics: IOSAnalytics(
                projectToken: PostHogProjectTokenKt.postHogProjectToken,
                host: PostHogHostKt.postHogHost,
                isDebug: isDebug
            )
        )

        SentryKt.doInitSentry(isDebug: isDebug)
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
