package sikv.lingomate.data.chat.model

import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import sikv.lingomate.data.chat.domain.ChatRecap
import sikv.lingomate.data.chat.mapping.toDomain
import kotlin.test.Test
import kotlin.test.assertEquals

class ChatRecapDTOTest {

    @Test
    fun schemaRequiresExactlyTheFieldsTheRecapIsReadFrom() {
        val names = ChatRecapDTO.serializer().descriptor.elementNames.toList()

        assertEquals(names, chatRecapSchema.getValue("properties").jsonObject.keys.toList())
        assertEquals(names, chatRecapSchema.getValue("required").jsonArray.map { it.jsonPrimitive.content })
    }

    @Test
    fun readsTheModelsReply() {
        val reply = """{"summary": "You ordered politely."}"""

        assertEquals(
            ChatRecap(summary = "You ordered politely."),
            Json.decodeFromString<ChatRecapDTO>(reply).toDomain()
        )
    }
}
