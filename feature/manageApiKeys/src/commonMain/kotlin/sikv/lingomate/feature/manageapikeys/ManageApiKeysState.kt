package sikv.lingomate.feature.manageapikeys

import kotlin.native.ObjCName

/**
 * UI state for the manage-API-keys feature.
 *
 * [storedProviders] lists the providers that currently have a key saved. The
 * key values themselves are never surfaced here.
 */
@ObjCName("ManageApiKeysState", exact = true)
data class ManageApiKeysState(
    val storedProviders: List<ApiKeyProvider> = emptyList(),
) {
    companion object {
        fun empty() = ManageApiKeysState()
    }
}
