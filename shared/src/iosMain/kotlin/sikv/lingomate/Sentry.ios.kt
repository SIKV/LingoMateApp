package sikv.lingomate

import cocoapods.Sentry.SentryFeedback
import cocoapods.Sentry.SentryFeedbackSourceCustom
import cocoapods.Sentry.SentrySDK
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
internal actual fun captureSentryFeedback(message: String, email: String?) {
    SentrySDK.captureFeedback(
        SentryFeedback(
            message = message,
            name = null,
            email = email,
            source = SentryFeedbackSourceCustom,
            associatedEventId = null,
            attachments = null
        )
    )
}
