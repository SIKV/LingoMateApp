package sikv.lingomate

import io.sentry.Sentry
import io.sentry.protocol.Feedback

internal actual fun captureSentryFeedback(message: String, email: String?) {
    Sentry.captureFeedback(
        Feedback(message).apply {
            contactEmail = email
        }
    )
}
