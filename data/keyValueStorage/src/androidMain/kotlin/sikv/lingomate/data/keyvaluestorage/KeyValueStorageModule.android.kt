package sikv.lingomate.data.keyvaluestorage

import android.content.Context
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun keyValueStorageModule(): Module = module {
    single<KeyValueStorage> {
        val context: Context = get()

        DataStoreKeyValueStorage(
            dataStore = createKeyValueDataStore {
                context.filesDir.resolve(KEY_VALUE_DATA_STORE_FILE_NAME).absolutePath
            }
        )
    }
}
