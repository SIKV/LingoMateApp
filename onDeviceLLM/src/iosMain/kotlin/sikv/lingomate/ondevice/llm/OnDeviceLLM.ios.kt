package sikv.lingomate.ondevice.llm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IOSOnDeviceLLM : OnDeviceLLM {

    override suspend fun checkAvailability(): Boolean {
        return iOSOnDeviceLLMProvider?.checkAvailability() ?: false
    }

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
    suspend fun checkAvailability(): Boolean
    suspend fun streamResponse(input: String, instructions: String): String?
}

private var iOSOnDeviceLLMProvider: IOSOnDeviceLLMProtocol? = null

fun setIOSOnDeviceLLMProvider(protocol: IOSOnDeviceLLMProtocol) {
    iOSOnDeviceLLMProvider = protocol
}
