package sikv.lingomate.ondevice.llm

import kotlinx.coroutines.flow.Flow

interface OnDeviceLLM {
    fun streamResponse(input: String, instructions: String): Flow<String?>
}
