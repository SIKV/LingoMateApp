package sikv.lingomate.data.chat.mapping

import sikv.lingomate.data.chat.domain.ChatRecap
import sikv.lingomate.data.chat.model.ChatRecapDTO

internal fun ChatRecapDTO.toDomain(): ChatRecap {
    return ChatRecap(
        summary = summary,
        score = score
    )
}
