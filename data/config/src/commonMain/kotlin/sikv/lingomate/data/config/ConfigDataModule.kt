package sikv.lingomate.data.config

import org.koin.dsl.module
import sikv.lingomate.data.config.datasource.FallbackConfigDataSource
import sikv.lingomate.data.config.datasource.RemoteConfigDataSource

val configDataModule = module {

    single {
        ConfigRepository(
            remoteConfigDataSource = get(),
            fallbackConfigDataSource = get()
        )
    }

    single {
        RemoteConfigDataSource(
            configApi = get()
        )
    }

    single {
        FallbackConfigDataSource()
    }
}
