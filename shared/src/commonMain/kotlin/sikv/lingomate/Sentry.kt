package sikv.lingomate

import io.sentry.kotlin.multiplatform.Sentry

// Generated per platform by the shared module's build script.
internal expect val sentryDsn: String

fun initSentry(isDebug: Boolean) {
    Sentry.init { options ->
        options.dsn = sentryDsn
        options.environment = if (isDebug) "debug" else "production"
    }
}
