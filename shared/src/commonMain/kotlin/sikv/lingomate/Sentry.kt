package sikv.lingomate

import io.sentry.kotlin.multiplatform.Sentry

// Generated per platform by the shared module's build script.
internal expect val sentryDsn: String

// The multiplatform SDK only attaches feedback to an existing event, so each platform sends it natively.
internal expect fun captureSentryFeedback(message: String, email: String?)

fun initSentry(isDebug: Boolean) {
    Sentry.init { options ->
        options.dsn = sentryDsn
        options.environment = if (isDebug) "debug" else "production"
    }
}
