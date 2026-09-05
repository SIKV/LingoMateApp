package sikv.lingomate.data.keyvaluestorage

/**
 * Platform-specific key/value storage for values that survive app restarts.
 *
 * Implementations persist values in plain, unencrypted OS storage:
 *  - Android: SharedPreferences.
 *  - iOS: NSUserDefaults.
 *
 * Nothing secret belongs here; use SecureStorage from `data:apiKeyStorage` for that.
 */
interface KeyValueStorage {
    fun put(key: String, value: String)
    fun get(key: String): String?
    fun remove(key: String)
    fun clear()
}
