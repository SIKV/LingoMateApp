package sikv.lingomate.data.chat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject

/** The recap as the model writes it, in the shape [chatRecapSchema] describes. */
@Serializable
internal data class ChatRecapDTO(
    @SerialName("summary")
    val summary: String,
)

/**
 * JSON Schema sent with the recap request, so the model's reply can be decoded straight into [ChatRecapDTO].
 *
 * Strict mode requires every property to be listed as required and no others to be allowed. kotlinx.serialization
 * can't generate a schema, so it's written by hand: keep it in sync with [ChatRecapDTO] (ChatRecapDTOTest checks this).
 */
internal val chatRecapSchema: JsonObject = buildJsonObject {
    put("type", "object")
    putJsonObject("properties") {
        putJsonObject("summary") { put("type", "string") }
    }
    putJsonArray("required") {
        add("summary")
    }
    put("additionalProperties", false)
}
