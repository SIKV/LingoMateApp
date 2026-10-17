package sikv.lingomate.feature.feedback

import kotlin.native.ObjCName

@ObjCName("FeedbackState", exact = true)
data class FeedbackState(
    val isSent: Boolean = false,
) {
    companion object {
        fun empty() = FeedbackState()
    }
}
