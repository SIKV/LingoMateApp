package sikv.lingomate.feature.manageapikeys

import sikv.lingomate.data.apikeystorage.SecureStorage

/** In-memory [SecureStorage] for tests. */
class FakeSecureStorage : SecureStorage {

    private val values = mutableMapOf<String, String>()

    override fun put(key: String, value: String) {
        values[key] = value
    }

    override fun get(key: String): String? = values[key]

    override fun remove(key: String) {
        values.remove(key)
    }

    override fun clear() {
        values.clear()
    }
}
