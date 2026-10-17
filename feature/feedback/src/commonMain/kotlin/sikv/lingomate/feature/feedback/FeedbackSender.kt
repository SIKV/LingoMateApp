package sikv.lingomate.feature.feedback

/** Delivers feedback written by the user: a bug report, an idea, or a feature request. */
fun interface FeedbackSender {
    fun send(message: String, email: String?)
}
