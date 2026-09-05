package sikv.lingomate.data.chat.storage

import sikv.lingomate.data.keyvaluestorage.KeyValueStorage

/** In-memory [KeyValueStorage] used to exercise [StartChatSelectionStorage] logic in tests. */
class FakeKeyValueStorage : KeyValueStorage {

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
