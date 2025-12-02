package sikv.lingomate.api

import org.koin.dsl.module

val apiModule = module {
    single { OpenAIApi() }
}
