package sikv.lingomate.feature.manageapikeys

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import sikv.lingomate.R
import sikv.lingomate.data.apikeystorage.ApiKeyProvider
import sikv.lingomate.feature.toLocalizedString
import sikv.lingomate.ui.theme.spacing

private val FabContentPadding = 88.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageApiKeysScreen(
    onBackPressed: () -> Unit,
    viewModel: ManageApiKeysViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    var providerPendingDeletion by remember { mutableStateOf<ApiKeyProvider?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.manage_api_keys_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                R.string.manage_api_keys_back_button_content_description
                            )
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(R.string.manage_api_keys_add_button)) },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null
                    )
                },
                onClick = { showAddDialog = true }
            )
        }
    ) { innerPadding ->
        if (state.storedProviders.isEmpty()) {
            EmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            LazyColumn(
                // Keeps the last item clear of the floating action button.
                contentPadding = PaddingValues(bottom = FabContentPadding),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(state.storedProviders) { provider ->
                    ApiKeyItem(
                        provider = provider,
                        onDelete = { providerPendingDeletion = provider }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddApiKeyDialog(
            storedProviders = state.storedProviders,
            onAdd = { provider, apiKey ->
                viewModel.addApiKey(provider, apiKey)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }

    providerPendingDeletion?.let { provider ->
        DeleteApiKeyDialog(
            provider = provider,
            onConfirm = {
                viewModel.removeApiKey(provider)
                providerPendingDeletion = null
            },
            onDismiss = { providerPendingDeletion = null }
        )
    }
}

@Composable
private fun ApiKeyItem(
    provider: ApiKeyProvider,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            )
    ) {
        Icon(
            imageVector = Icons.Rounded.Key,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = provider.toLocalizedString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.tiny))

            Text(
                text = stringResource(R.string.manage_api_keys_key_saved),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = stringResource(
                    R.string.manage_api_keys_delete_button_content_description
                ),
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.extraMedium)
    ) {
        Icon(
            imageVector = Icons.Rounded.Key,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        Text(
            text = stringResource(R.string.manage_api_keys_empty_title),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

        Text(
            text = stringResource(R.string.manage_api_keys_empty_info),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AddApiKeyDialog(
    storedProviders: List<ApiKeyProvider>,
    onAdd: (ApiKeyProvider, String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedProvider by remember {
        mutableStateOf(
            ApiKeyProvider.entries.firstOrNull { it !in storedProviders }
                ?: ApiKeyProvider.entries.first()
        )
    }
    var apiKey by remember { mutableStateOf("") }
    var keyVisible by remember { mutableStateOf(false) }
    var showReplaceConfirmation by remember { mutableStateOf(false) }

    val replacesExistingKey = selectedProvider in storedProviders

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (replacesExistingKey) {
                    stringResource(R.string.manage_api_keys_replace_dialog_title)
                } else {
                    stringResource(R.string.manage_api_keys_add_dialog_title)
                }
            )
        },
        text = {
            Column {
                ProviderSelector(
                    selected = selectedProvider,
                    onSelect = { selectedProvider = it }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    label = { Text(stringResource(R.string.manage_api_keys_key_label)) },
                    singleLine = true,
                    visualTransformation = if (keyVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(onClick = { keyVisible = !keyVisible }) {
                            Icon(
                                imageVector = if (keyVisible) {
                                    Icons.Rounded.VisibilityOff
                                } else {
                                    Icons.Rounded.Visibility
                                },
                                contentDescription = if (keyVisible) {
                                    stringResource(
                                        R.string.manage_api_keys_hide_key_content_description
                                    )
                                } else {
                                    stringResource(
                                        R.string.manage_api_keys_show_key_content_description
                                    )
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Overwriting a stored key destroys it, so confirm first.
                    if (replacesExistingKey) {
                        showReplaceConfirmation = true
                    } else {
                        onAdd(selectedProvider, apiKey.trim())
                    }
                },
                enabled = apiKey.isNotBlank()
            ) {
                Text(stringResource(R.string.manage_api_keys_save_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.manage_api_keys_cancel_button))
            }
        }
    )

    if (showReplaceConfirmation) {
        ReplaceApiKeyDialog(
            provider = selectedProvider,
            onConfirm = { onAdd(selectedProvider, apiKey.trim()) },
            // Leaves the entered key intact so it can be saved after all.
            onDismiss = { showReplaceConfirmation = false }
        )
    }
}

@Composable
private fun ReplaceApiKeyDialog(
    provider: ApiKeyProvider,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.manage_api_keys_replace_confirm_dialog_title)) },
        text = {
            Text(
                stringResource(
                    R.string.manage_api_keys_replace_confirm_dialog_message,
                    provider.toLocalizedString()
                )
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.manage_api_keys_replace_confirm_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.manage_api_keys_cancel_button))
            }
        }
    )
}

@Composable
private fun ProviderSelector(
    selected: ApiKeyProvider,
    onSelect: (ApiKeyProvider) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(vertical = MaterialTheme.spacing.small)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.manage_api_keys_provider_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

                Text(
                    text = selected.toLocalizedString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }

            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ApiKeyProvider.entries.forEach { provider ->
                DropdownMenuItem(
                    text = { Text(provider.toLocalizedString()) },
                    trailingIcon = if (provider == selected) {
                        {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        null
                    },
                    onClick = {
                        onSelect(provider)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DeleteApiKeyDialog(
    provider: ApiKeyProvider,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.manage_api_keys_delete_dialog_title)) },
        text = {
            Text(
                stringResource(
                    R.string.manage_api_keys_delete_dialog_message,
                    provider.toLocalizedString()
                )
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.manage_api_keys_delete_dialog_confirm),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.manage_api_keys_cancel_button))
            }
        }
    )
}
