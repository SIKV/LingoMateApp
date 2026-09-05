package sikv.lingomate.data.keyvaluestorage

/**
 * Key/value storage for values that survive app restarts.
 *
 * Reads and writes are suspending: they touch the file system, and a write only returns once
 * the value is durably stored.
 *
 * Values are stored unencrypted. Nothing secret belongs here; use SecureStorage from
 * `data:apiKeyStorage` for that.
 */
interface KeyValueStorage {
    suspend fun put(key: String, value: String)
    suspend fun get(key: String): String?
    suspend fun remove(key: String)
    suspend fun clear()
}
