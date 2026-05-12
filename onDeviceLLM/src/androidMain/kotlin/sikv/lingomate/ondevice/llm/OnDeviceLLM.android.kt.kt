package sikv.lingomate.ondevice.llm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AndroidOnDeviceLLM : OnDeviceLLM {

    override fun streamResponse(
        input: String,
        instructions: String
    ): Flow<String?> {
        return flow {
            emit("Android")
        }
    }
}
