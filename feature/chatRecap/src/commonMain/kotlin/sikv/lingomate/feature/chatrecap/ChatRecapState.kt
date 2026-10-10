package sikv.lingomate.feature.chatrecap

import sikv.lingomate.data.chat.domain.ChatRecap
import kotlin.native.ObjCName

@ObjCName("ChatRecapState", exact = true)
data class ChatRecapState(
    val recap: ChatRecap? = null,
    val hasFailed: Boolean = false,
) {
    companion object {
        fun empty() = ChatRecapState()
    }
}
