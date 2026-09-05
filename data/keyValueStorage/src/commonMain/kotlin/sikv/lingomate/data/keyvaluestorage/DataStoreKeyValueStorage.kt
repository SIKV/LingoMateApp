package sikv.lingomate.data.keyvaluestorage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException
import sikv.lingomate.logger.Log

/** [KeyValueStorage] backed by Preferences DataStore. */
internal class DataStoreKeyValueStorage(
    private val dataStore: DataStore<Preferences>,
) : KeyValueStorage {

    override suspend fun put(key: String, value: String) {
        edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun get(key: String): String? {
        return dataStore.data
            .catch { throwable ->
                // A file that cannot be read leaves the stored value unknown, not the app broken.
                if (throwable is IOException) {
                    Log.e(throwable) { "Failed to read $key." }
                    emit(emptyPreferences())
                } else {
                    throw throwable
                }
            }
            .map { preferences -> preferences[stringPreferencesKey(key)] }
            .first()
    }

    override suspend fun remove(key: String) {
        edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
        }
    }

    override suspend fun clear() {
        edit { preferences ->
            preferences.clear()
        }
    }

    private suspend fun edit(transform: (MutablePreferences) -> Unit) {
        try {
            dataStore.edit(transform)
        } catch (e: IOException) {
            Log.e(e) { "Failed to write to the key value storage." }
        }
    }
}
