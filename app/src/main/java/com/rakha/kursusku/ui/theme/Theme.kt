package com.rakha.kursusku.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkCoralPrimary,
    onPrimary = Color.White,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = DarkOnSurface,
    secondary = DarkCoralSecondary, // High contrast bright coral for "KursusKu" title in dark mode
    onSecondary = DarkBackground,
    secondaryContainer = Color(0xFF431407),
    onSecondaryContainer = DarkCoralSecondary,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = DarkOutline,
    error = Color(0xFFF87171),
)

private val LightColorScheme = lightColorScheme(
    primary = WarmNavy,
    onPrimary = Color.White,
    primaryContainer = WarmNavyContainer,
    onPrimaryContainer = WarmNavy,
    secondary = SoftCoral,
    onSecondary = Color.White,
    secondaryContainer = SoftCoralContainer,
    onSecondaryContainer = SoftCoral,
    background = OffWhite,
    onBackground = WarmNavy,
    surface = SurfaceWhite,
    onSurface = WarmNavy,
    surfaceVariant = WarmNavyContainer,
    onSurfaceVariant = Color(0xFF64748B),
    outline = Color(0xFFCBD5E1),
    error = Color(0xFFDC2626),
)

@Composable
fun KursuskuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set default dynamicColor to false so the custom KursusKu brand palette is used
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}