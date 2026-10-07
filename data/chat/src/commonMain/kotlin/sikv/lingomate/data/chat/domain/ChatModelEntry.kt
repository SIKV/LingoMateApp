package sikv.lingomate.data.chat.domain

/**
 * A model as the config offers it. The tier stays out of [ChatModel], which only names the model: the saved
 * selection is a [ChatModel] too, and it has to keep matching when the config moves a model to another tier.
 */
data class ChatModelEntry(
    val chatModel: ChatModel,
    // Null for a tier this build doesn't know, which only costs the model its label.
    val tier: ChatModelTier?
)
