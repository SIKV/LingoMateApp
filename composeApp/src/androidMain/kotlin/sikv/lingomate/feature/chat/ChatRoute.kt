package sikv.lingomate.feature.chat

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface ChatRoute : NavKey {

    @Serializable
    data object StartChat : ChatRoute

    @Serializable
    data object Chat : ChatRoute
}
