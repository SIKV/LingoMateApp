package sikv.lingomate.data.chat.domain

import kotlinx.serialization.Serializable
import kotlin.native.ObjCName

@Serializable
@ObjCName("ChatConfig", exact = true)
data class ChatConfig(
    val chatModel: ChatModel,
    val practiceLanguage: Language,
    val assistantLanguage: Language,
    val practiceType: PracticeType,
)
