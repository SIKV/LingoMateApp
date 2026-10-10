package sikv.lingomate.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer

private const val SHIMMER_DURATION_MILLIS = 1200

fun Modifier.shimmer(): Modifier = composed {
    val progress = rememberInfiniteTransition(label = "shimmer").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(SHIMMER_DURATION_MILLIS, easing = LinearEasing)),
        label = "shimmer"
    )

    this
        // Drawing offscreen lets the highlight below land only on what the content drew, not on the gaps between.
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()

            // The band starts fully left of the content and ends fully right of it, so each sweep enters and leaves.
            val bandWidth = size.width / 2
            val center = -bandWidth + progress.value * (size.width + bandWidth * 2)
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.3f), Color.Transparent),
                    start = Offset(center - bandWidth / 2, 0f),
                    end = Offset(center + bandWidth / 2, 0f)
                ),
                blendMode = BlendMode.SrcAtop
            )
        }
}
