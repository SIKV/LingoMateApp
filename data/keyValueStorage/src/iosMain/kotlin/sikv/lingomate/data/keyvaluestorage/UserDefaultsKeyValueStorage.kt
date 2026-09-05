package sikv.lingomate.data.keyvaluestorage

import platform.Foundation.NSUserDefaults

/**
 * [KeyValueStorage] backed by NSUserDefaults.
 *
 * Values live in their own suite rather than the standard domain, so [clear] only
 * drops what this storage wrote.
 */
internal class UserDefaultsKeyValueStorage(
    private val suiteName: String = SUITE_NAME,
) : KeyValueStorage {

    private val userDefaults = NSUserDefaults(suiteName = suiteName)

    override fun put(key: String, value: String) {
        userDefaults.setObject(value, forKey = key)
    }

    override fun get(key: String): String? {
        return userDefaults.stringForKey(key)
    }

    override fun remove(key: String) {
        userDefaults.removeObjectForKey(key)
    }

    override fun clear() {
        userDefaults.removePersistentDomainForName(suiteName)
    }

    private companion object {
        const val SUITE_NAME = "sikv.lingomate.data.keyValueStorage"
    }
}
