package dev.amal.onthewakelivekmm.android.core.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorPalette = darkColorScheme(
    primary = PrimaryBlue80,
    onPrimary = PrimaryBlue20,
    primaryContainer = PrimaryBlue30,
    onPrimaryContainer = PrimaryBlue90,
    inversePrimary = PrimaryBlue40,
    secondary = SecondaryBlue80,
    onSecondary = SecondaryBlue20,
    secondaryContainer = SecondaryBlue30,
    onSecondaryContainer = SecondaryBlue90,
    tertiary = TertiaryPurple80,
    onTertiary = TertiaryPurple20,
    tertiaryContainer = TertiaryPurple30,
    onTertiaryContainer = TertiaryPurple90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = Neutral10,
    onBackground = Neutral90,
    surface = Neutral10,
    onSurface = Neutral80,
    inverseSurface = PrimaryBlue90,
    inverseOnSurface = PrimaryBlue10,
    surfaceVariant = NeutralVariant30,
    onSurfaceVariant = NeutralVariant80,
    outline = NeutralVariant60
)

private val LightColorPalette = lightColorScheme(
    primary = PrimaryBlue40,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlue90,
    onPrimaryContainer = PrimaryBlue10,
    inversePrimary = PrimaryBlue80,
    secondary = SecondaryBlue40,
    onSecondary = Color.White,
    secondaryContainer = PrimaryBlue90,
    onSecondaryContainer = PrimaryBlue10,
    tertiary = TertiaryPurple40,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryPurple90,
    onTertiaryContainer = TertiaryPurple10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = Neutral99,
    onBackground = Neutral10,
    surface = Neutral99,
    onSurface = Neutral10,
    inverseSurface = PrimaryBlue20,
    inverseOnSurface = PrimaryBlue95,
    surfaceVariant = NeutralVariant90,
    onSurfaceVariant = NeutralVariant30,
    outline = NeutralVariant50
)

@Composable
fun OnTheWakeLiveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val useDynamicColors = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colors = when {
        useDynamicColors && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        useDynamicColors && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}