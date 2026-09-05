package sikv.lingomate.data.keyvaluestorage

import org.koin.core.module.Module
import org.koin.dsl.module

actual fun keyValueStorageModule(): Module = module {
    single<KeyValueStorage> { UserDefaultsKeyValueStorage() }
}
