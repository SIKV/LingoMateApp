package sikv.lingomate.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.unit.sp

private val DefaultTypography = Typography()

val Typography = DefaultTypography.copy(
    titleMedium = DefaultTypography.titleMedium.copy(
        fontSize = 19.sp,
        lineHeight = 24.sp
    )
)
