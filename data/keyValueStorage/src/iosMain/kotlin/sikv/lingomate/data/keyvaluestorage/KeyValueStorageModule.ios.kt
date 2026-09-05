package sikv.lingomate.data.keyvaluestorage

import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun keyValueStorageModule(): Module = module {
    single<KeyValueStorage> {
        DataStoreKeyValueStorage(
            dataStore = createKeyValueDataStore { dataStoreFilePath() }
        )
    }
}

/** The DataStore file, kept in the app's documents directory. */
@OptIn(ExperimentalForeignApi::class)
private fun dataStoreFilePath(): String {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )

    return requireNotNull(documentDirectory?.path) { "Unable to locate the documents directory." } +
        "/$KEY_VALUE_DATA_STORE_FILE_NAME"
}
