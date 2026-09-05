package sikv.lingomate.data.keyvaluestorage

import android.content.Context
import android.content.SharedPreferences

/** [KeyValueStorage] backed by [SharedPreferences]. */
internal class SharedPreferencesKeyValueStorage(
    context: Context,
) : KeyValueStorage {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
    }

    override fun put(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun get(key: String): String? {
        return prefs.getString(key, null)
    }

    override fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    override fun clear() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val PREFS_FILE_NAME = "key_value_storage"
    }
}
