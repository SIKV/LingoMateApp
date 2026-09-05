package sikv.lingomate.data.keyvaluestorage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import okio.Path.Companion.toPath

/** The DataStore file name. DataStore requires the `.preferences_pb` suffix. */
internal const val KEY_VALUE_DATA_STORE_FILE_NAME = "key_value_storage.preferences_pb"

/**
 * Creates the DataStore instance backing [KeyValueStorage], reading and writing on the
 * IO scope DataStore sets up for itself.
 *
 * A file may only be held by one DataStore at a time, so callers must keep the result a
 * singleton.
 */
internal fun createKeyValueDataStore(
    producePath: () -> String,
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )
}

/**
 * Creates the DataStore instance backing [KeyValueStorage] on a caller owned [scope], which
 * holds the file until it is cancelled. Useful to hand the file over to another instance.
 */
internal fun createKeyValueDataStore(
    scope: CoroutineScope,
    producePath: () -> String,
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        scope = scope,
        produceFile = { producePath().toPath() }
    )
}
