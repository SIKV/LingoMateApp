package sikv.lingomate.data.apikeystorage

import org.koin.core.module.Module
import org.koin.dsl.module

actual fun apiKeyStorageModule(): Module = module {
    single<SecureStorage> { EncryptedSharedPreferencesSecureStorage(context = get()) }
    single { ApiKeyStorage(secureStorage = get()) }
}
