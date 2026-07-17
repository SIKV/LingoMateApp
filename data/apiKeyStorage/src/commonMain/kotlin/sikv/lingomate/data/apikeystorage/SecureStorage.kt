package sikv.lingomate.data.apikeystorage

/**
 * Platform-specific secure key/value storage.
 *
 * Implementations must persist values in an OS-backed secure store:
 *  - Android: EncryptedSharedPreferences (Android Keystore backed).
 *  - iOS: Keychain.
 */
interface SecureStorage {
    fun put(key: String, value: String)
    fun get(key: String): String?
    fun remove(key: String)
    fun clear()
}
