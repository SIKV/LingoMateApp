package sikv.lingomate.ondevice.llm

import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.prompt.Generation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AndroidOnDeviceLLM : OnDeviceLLM {

    override suspend fun checkAvailability(): Boolean {
        val generativeModel = Generation.getClient()

        return when ( generativeModel.checkStatus()) {
            FeatureStatus.UNAVAILABLE,
            FeatureStatus.DOWNLOADABLE,
            FeatureStatus.DOWNLOADING ->
                 false
            FeatureStatus.AVAILABLE ->
                 true
            else -> false
        }
    }

    override fun streamResponse(
        input: String,
        instructions: String
    ): Flow<String?> {
        // TODO: Implement.
        return flow {
            emit(null)
        }
    }
}
