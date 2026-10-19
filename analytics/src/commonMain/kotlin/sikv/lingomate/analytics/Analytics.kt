package sikv.lingomate.analytics

import kotlin.native.ObjCName

/**
 * Tracks product analytics events from shared code. Each app implements it with
 * its platform's SDK and hands it to Koin at startup.
 */
@ObjCName("Analytics", exact = true)
fun interface Analytics {
    fun track(event: AnalyticsEvent)
}
