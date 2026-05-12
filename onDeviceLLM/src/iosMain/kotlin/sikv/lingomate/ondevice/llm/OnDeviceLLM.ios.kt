package sikv.lingomate.ondevice.llm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IOSOnDeviceLLM : OnDeviceLLM {

    override fun streamResponse(
        input: String,
        instructions: String
    ): Flow<String?> {
        return flow {
            emit(iOSOnDeviceLLMProvider?.streamResponse(input, instructions))
        }
    }
}

@ObjCName("IOSOnDeviceLLMProtocol", exact = true)
interface IOSOnDeviceLLMProtocol {
    fun streamResponse(input: String, instructions: String): String
}

private var iOSOnDeviceLLMProvider: IOSOnDeviceLLMProtocol? = null

fun setIOSOnDeviceLLMProvider(protocol: IOSOnDeviceLLMProtocol) {
    iOSOnDeviceLLMProvider = protocol
}
