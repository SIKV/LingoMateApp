package sikv.lingomate.ondevice.llm

import kotlinx.coroutines.flow.Flow

interface OnDeviceLLM {
    suspend fun checkAvailability(): Boolean
    fun streamResponse(input: String, instructions: String): Flow<String?>
}
