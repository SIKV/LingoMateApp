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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import sikv.lingomate.R
import sikv.lingomate.ui.theme.spacing

@Composable
fun StartChatScreen(
    onNavigateToChat: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Section(
                title = stringResource(R.string.start_chat_language_label),
                modifier = Modifier
                    .padding(
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.medium
                    )
            ) {
                ChatLanguageDropdown()
            }

            Section(
                title = stringResource(R.string.start_chat_length_label),
                modifier = Modifier
                    .padding(
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.large
                )
            ) {
                ChatLengthOptions()
            }

            StartChatButton(
                onClick = onNavigateToChat,
                modifier = Modifier
                    .padding(MaterialTheme.spacing.large)
            )
        }
    }
}

@Composable
private fun Section(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(MaterialTheme.spacing.extraMedium)
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        content()
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
            .fillMaxWidth()
            .background(brush, shape = RoundedCornerShape(32.dp))
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
            imageVector = Icons.AutoMirrored.Default.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = MaterialTheme.spacing.medium)
        )
    }
}

@Composable
fun ChatLanguageDropdown() {
    var expanded by remember { mutableStateOf(false) }

    // TODO: Use string res.
    var selectedOption by remember { mutableStateOf("Select Language") }
    // TODO: Replace with actual data.
    val options = listOf("English", "Spanish")

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        FilledTonalButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedOption)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ChatLengthOptions() {
    // TODO: Replace with actual data.
    val options = listOf("Short", "Medium", "Long")
    var selectedIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)
                ) {
                    Text(option)
                }
            }
        }
    }
}
