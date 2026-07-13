package sikv.lingomate.feature.startchat

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import sikv.lingomate.R
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.PracticeLanguage
import sikv.lingomate.data.chat.domain.PracticeType
import sikv.lingomate.data.chat.domain.TranslationLanguage
import sikv.lingomate.feature.toLocalizedString
import sikv.lingomate.ui.isLandscape
import sikv.lingomate.ui.theme.radius
import sikv.lingomate.ui.theme.spacing

@Composable
fun StartChatScreen(
    onNavigateToChat: () -> Unit,
    viewModel: StartChatViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val isLandscape = isLandscape()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.extraMedium
                    )
            ) {
                val contentModifier = if (isLandscape) {
                    Modifier.fillMaxWidth(fraction = 0.6f)
                } else {
                    Modifier.fillMaxWidth()
                }

                Header(showIcon = !isLandscape)

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))

                ChatConfigCard(
                    state = state,
                    onSelectChatModel = viewModel::selectChatModel,
                    onSelectPracticeLanguage = viewModel::selectPracticeLanguage,
                    onSelectTranslationLanguage = viewModel::selectTranslationLanguage,
                    onSelectPracticeType = viewModel::selectPracticeType,
                    modifier = contentModifier
                )

                StartChatButton(
                    onClick = onNavigateToChat,
                    modifier = contentModifier
                        .padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.extraMedium
                        )
                )
            }
        }
    }
}

@Composable
private fun Header(
    showIcon: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (showIcon) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_auto_awesome_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }

        Text(
            text = stringResource(R.string.start_chat_greeting),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Text(
            text = stringResource(R.string.start_chat_info),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ChatConfigCard(
    state: StartChatState,
    onSelectChatModel: (ChatModel) -> Unit,
    onSelectPracticeLanguage: (PracticeLanguage) -> Unit,
    onSelectTranslationLanguage: (TranslationLanguage) -> Unit,
    onSelectPracticeType: (PracticeType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = RoundedCornerShape(MaterialTheme.radius.medium)
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(MaterialTheme.radius.medium)
            )
    ) {
        SelectorRow(
            label = stringResource(R.string.start_chat_chat_model_label),
            options = state.chatModels,
            selected = state.selectedChatModel,
            onSelect = onSelectChatModel,
            optionLabel = { it.toLocalizedString() }
        )

        SelectorDivider()

        SelectorRow(
            label = stringResource(R.string.start_chat_practice_language_label),
            options = state.practiceLanguages,
            selected = state.selectedPracticeLanguage,
            onSelect = onSelectPracticeLanguage,
            optionLabel = { it.toLocalizedString() }
        )

        SelectorDivider()

        SelectorRow(
            label = stringResource(R.string.start_chat_translation_language_label),
            options = state.translationLanguages,
            selected = state.selectedTranslationLanguage,
            onSelect = onSelectTranslationLanguage,
            optionLabel = { it.toLocalizedString() }
        )

        SelectorDivider()

        SelectorRow(
            label = stringResource(R.string.start_chat_practice_type_label),
            options = state.practiceTypes,
            selected = state.selectedPracticeType,
            onSelect = onSelectPracticeType,
            optionLabel = { it.toLocalizedString() }
        )
    }
}

@Composable
private fun SelectorDivider() {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.surfaceDim,
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraMedium)
    )
}

@Composable
private fun <T : Any> SelectorRow(
    label: String,
    options: List<T>,
    selected: T?,
    onSelect: (T) -> Unit,
    optionLabel: @Composable (T) -> String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = options.isNotEmpty()) { expanded = true }
                .padding(
                    horizontal = MaterialTheme.spacing.extraMedium,
                    vertical = MaterialTheme.spacing.medium
                )
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

                Text(
                    text = selected?.let { optionLabel(it) }
                        ?: stringResource(R.string.start_chat_not_selected),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = if (selected != null) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
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
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    trailingIcon = if (option == selected) {
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
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun StartChatButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.tertiary
    )

    val transition = rememberInfiniteTransition()

    val offsetX by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(offsetX, 0f),
        end = Offset(offsetX + 300f, 300f),
        tileMode = TileMode.Mirror
    )

    Box(
        modifier = modifier
            .height(64.dp)
            .background(brush, shape = RoundedCornerShape(MaterialTheme.radius.extraMedium))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.start_chat_start_button),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier.align(Alignment.Center)
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

        Icon(
            painter = painterResource(R.drawable.ic_auto_awesome_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = MaterialTheme.spacing.extraMedium)
        )
    }
}
