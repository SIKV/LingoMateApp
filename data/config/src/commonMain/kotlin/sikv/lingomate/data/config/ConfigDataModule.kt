package sikv.lingomate.data.config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import sikv.lingomate.data.config.datasource.CachedConfigDataSource
import sikv.lingomate.data.config.datasource.FallbackConfigDataSource
import sikv.lingomate.data.config.datasource.RemoteConfigDataSource

val configDataModule = module {

    single {
        ConfigRepository(
            remoteConfigDataSource = get(),
            cachedConfigDataSource = get(),
            fallbackConfigDataSource = get(),
            refreshScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        )
    }

    single {
        RemoteConfigDataSource(
            remoteConfigApi = get()
        )
    }

    single {
        CachedConfigDataSource(
            keyValueStorage = get()
        )
    }

    single {
        FallbackConfigDataSource()
    }
}
