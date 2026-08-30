package sikv.lingomate.data.chat.domain

import kotlinx.serialization.Serializable
import kotlin.native.ObjCName

/**
 * A language the user can pick for a chat, on either side of it: the one they practise or the one the assistant
 * explains in. Picking the same one on both sides is allowed and runs the session monolingually.
 */
@Serializable
@ObjCName("Language", exact = true)
enum class Language {
    ARABIC,
    CZECH,
    DANISH,
    DUTCH,
    ENGLISH,
    FINNISH,
    FRENCH,
    GERMAN,
    GREEK,
    HUNGARIAN,
    ITALIAN,
    JAPANESE,
    KOREAN,
    NORWEGIAN,
    POLISH,
    PORTUGUESE,
    ROMANIAN,
    SPANISH,
    SWEDISH,
    TURKISH,
    UKRAINIAN,
}
