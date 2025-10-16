package sikv.lingomate.feature.chat

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface ChatRoute : NavKey {

    @Serializable
    data object Root : ChatRoute

    @Serializable
    data object ChatDetails : ChatRoute
}
