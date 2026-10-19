package sikv.lingomate.di

import org.koin.dsl.module
import sikv.lingomate.analytics.Analytics

fun analyticsModule(analytics: Analytics) = module {

    single<Analytics> { analytics }
}
