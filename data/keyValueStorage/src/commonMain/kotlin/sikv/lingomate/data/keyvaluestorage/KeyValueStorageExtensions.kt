package sikv.lingomate.data.keyvaluestorage

import kotlin.enums.enumEntries
import sikv.lingomate.logger.Log

/** Stores [value] under [key], or drops the entry when it is null. */
suspend fun KeyValueStorage.putOrRemove(key: String, value: String?) {
    if (value == null) {
        remove(key)
    } else {
        put(key, value)
    }
}

/**
 * Stores [value] by its name.
 *
 * Names, not ordinals: reordering an enum then keeps stored values readable, and an entry
 * that is later removed reads back as null rather than resolving to an unrelated one.
 */
suspend fun <T : Enum<T>> KeyValueStorage.putEnum(key: String, value: T?) {
    putOrRemove(key, value?.name)
}

/** Reads the entry stored by [putEnum], or null when it is missing or no longer known. */
suspend inline fun <reified T : Enum<T>> KeyValueStorage.getEnum(key: String): T? {
    return getEnum(key, enumEntries<T>())
}

/**
 * Reads the entry stored by [putEnum] as one of [entries], or null when it is missing or no
 * longer one of them.
 */
suspend fun <T : Enum<T>> KeyValueStorage.getEnum(key: String, entries: List<T>): T? {
    val name = get(key) ?: return null

    return entries.firstOrNull { it.name == name }
        ?: run {
            Log.w { "Dropping the stored value of $key: $name is no longer a known entry." }
            null
        }
}
