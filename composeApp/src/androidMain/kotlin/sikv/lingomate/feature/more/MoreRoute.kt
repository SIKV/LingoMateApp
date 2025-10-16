package sikv.lingomate.feature.more

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface MoreRoute : NavKey {

    @Serializable
    data object Root : MoreRoute
}
