package sikv.lingomate.data.keyvaluestorage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import okio.Path.Companion.toPath

/** The DataStore file name. DataStore requires the `.preferences_pb` suffix. */
internal const val KEY_VALUE_DATA_STORE_FILE_NAME = "key_value_storage.preferences_pb"

internal fun createKeyValueDataStore(
    producePath: () -> String,
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )
}

internal fun createKeyValueDataStore(
    scope: CoroutineScope,
    producePath: () -> String,
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        scope = scope,
        produceFile = { producePath().toPath() }
    )
}
