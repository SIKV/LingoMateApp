package sikv.lingomate.ondevice.llm

import org.koin.core.module.Module
import org.koin.dsl.module

actual fun onDeviceLLMModule(): Module = module {
    single<OnDeviceLLM> { AndroidOnDeviceLLM() }
}
