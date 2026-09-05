package sikv.lingomate.data.chat.storage

import sikv.lingomate.data.keyvaluestorage.KeyValueStorage

/** In-memory [KeyValueStorage] used to exercise [StartChatSelectionStorage] logic in tests. */
class FakeKeyValueStorage : KeyValueStorage {

    val entries = mutableMapOf<String, String>()

    override suspend fun put(key: String, value: String) {
        entries[key] = value
    }

    override suspend fun get(key: String): String? {
        return entries[key]
    }

    override suspend fun remove(key: String) {
        entries.remove(key)
    }

    override suspend fun clear() {
        entries.clear()
    }
}
