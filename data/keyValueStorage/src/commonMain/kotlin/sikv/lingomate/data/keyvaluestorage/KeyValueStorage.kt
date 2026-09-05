package sikv.lingomate.data.keyvaluestorage

interface KeyValueStorage {
    suspend fun put(key: String, value: String)
    suspend fun get(key: String): String?
    suspend fun remove(key: String)
    suspend fun clear()
}
