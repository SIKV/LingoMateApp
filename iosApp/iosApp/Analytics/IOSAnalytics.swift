import Foundation
import PostHog
import Shared

class IOSAnalytics : Analytics {

    init(projectToken: String, host: String, isDebug: Bool) {
        guard !projectToken.isEmpty, !host.isEmpty else { return }

        let config = PostHogConfig(projectToken: projectToken, host: host)
        // SwiftUI screens show up as hosting controllers, so automatic screen views carry no names.
        config.captureScreenViews = false
        PostHogSDK.shared.setup(config)
        PostHogSDK.shared.register(["environment": isDebug ? "debug" : "production"])
    }

    func track(event: any AnalyticsEvent) {
        PostHogSDK.shared.capture(event.name, properties: event.properties)
    }
}
