package sikv.lingomate.feature.feedback

fun interface FeedbackSender {
    fun send(message: String, email: String?)
}
