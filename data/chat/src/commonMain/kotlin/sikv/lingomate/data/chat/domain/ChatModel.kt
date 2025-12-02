package sikv.lingomate.data.chat.domain

import kotlin.native.ObjCName

@ObjCName("ChatModel", exact = true)
enum class ChatModel(id: String) {
    GPT_5_NANO("gpt-5-nano"),
    GPT_5_MINI("gpt-5-mini"),
    GPT_5_1("gpt-5.1")
}
