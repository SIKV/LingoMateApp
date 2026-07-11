package sikv.lingomate.data.chat.domain

import kotlin.native.ObjCName

@ObjCName("ChatConfig", exact = true)
data class ChatConfig(
    val chatModel: ChatModel,
    val practiceLanguage: PracticeLanguage,
    val translationLanguage: TranslationLanguage,
    val practiceType: PracticeType,
)
