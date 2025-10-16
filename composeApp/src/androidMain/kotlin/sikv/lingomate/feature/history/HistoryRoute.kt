package sikv.lingomate.feature.history

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface HistoryRoute : NavKey {

    @Serializable
    data object Root : HistoryRoute

    @Serializable
    data object ChatDetails : HistoryRoute
}
