package sikv.lingomate.data.apikeystorage

/** In-memory [SecureStorage] used to exercise [ApiKeyStorage] logic in tests. */
class FakeSecureStorage : SecureStorage {

    val entries = mutableMapOf<String, String>()

    override fun put(key: String, value: String) {
        entries[key] = value
    }

    override fun get(key: String): String? {
        return entries[key]
    }

    override fun remove(key: String) {
        entries.remove(key)
    }

    override fun clear() {
        entries.clear()
    }
}
