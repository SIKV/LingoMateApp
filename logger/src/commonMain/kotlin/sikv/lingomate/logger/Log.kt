package sikv.lingomate.logger

import co.touchlab.kermit.Severity
import co.touchlab.kermit.Logger as Kermit

private const val DEFAULT_TAG = "LingoMate"

/**
 * Shared logger, available to every module. No setup needed at the call site:
 * `Log.e(error) { "Failed to send message." }`
 *
 * Pass [tag] to group logs of a single class or feature, otherwise they fall
 * back to [DEFAULT_TAG]. Writes to Logcat on Android and to os_log on iOS.
 */
object Log {

    fun d(throwable: Throwable? = null, tag: String? = null, message: () -> String) {
        log(Severity.Debug, throwable, tag, message)
    }

    fun i(throwable: Throwable? = null, tag: String? = null, message: () -> String) {
        log(Severity.Info, throwable, tag, message)
    }

    fun w(throwable: Throwable? = null, tag: String? = null, message: () -> String) {
        log(Severity.Warn, throwable, tag, message)
    }

    fun e(throwable: Throwable? = null, tag: String? = null, message: () -> String) {
        log(Severity.Error, throwable, tag, message)
    }

    // The message is only built when the severity is actually loggable.
    private fun log(severity: Severity, throwable: Throwable?, tag: String?, message: () -> String) {
        if (Kermit.config.minSeverity <= severity) {
            Kermit.log(severity, tag ?: DEFAULT_TAG, throwable, message())
        }
    }
}
