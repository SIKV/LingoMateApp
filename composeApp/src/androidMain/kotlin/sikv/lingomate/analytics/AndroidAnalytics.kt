package sikv.lingomate.analytics

import android.app.Application
import com.posthog.PostHog
import com.posthog.android.PostHogAndroid
import com.posthog.android.PostHogAndroidConfig

class AndroidAnalytics(
    application: Application,
    projectToken: String,
    host: String,
    isDebug: Boolean
) : Analytics {

    init {
        if (projectToken.isNotEmpty() && host.isNotEmpty()) {
            val config = PostHogAndroidConfig(projectToken = projectToken, host = host).apply {
                // Every Compose screen lives in one activity, so automatic screen views are all the same.
                captureScreenViews = false
            }
            PostHogAndroid.setup(application, config)
            PostHog.register("environment", if (isDebug) "debug" else "production")
        }
    }

    override fun track(event: AnalyticsEvent) {
        PostHog.capture(event = event.name, properties = event.properties)
    }
}
