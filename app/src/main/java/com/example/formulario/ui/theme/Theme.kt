package com.example.formulario.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = PrimaryDarkVariant,
    onPrimaryContainer = androidx.compose.ui.graphics.Color.White,
    
    secondary = Secondary,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = SecondaryVariant,
    onSecondaryContainer = androidx.compose.ui.graphics.Color.White,
    
    tertiary = Secondary,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    
    background = BackgroundDark,
    onBackground = androidx.compose.ui.graphics.Color.White,
    
    surface = SurfaceDarkMode,
    onSurface = androidx.compose.ui.graphics.Color.White,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextTertiary,
    
    error = Error,
    onError = androidx.compose.ui.graphics.Color.White,
    errorContainer = ErrorContainer,
    onErrorContainer = Error,
    
    outline = Border,
    outlineVariant = BorderLight
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = PrimaryVariant,
    
    secondary = Secondary,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = SecondaryVariant,
    
    tertiary = Secondary,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    
    background = SurfaceLight,
    onBackground = TextPrimary,
    
    surface = androidx.compose.ui.graphics.Color.White,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,
    
    error = Error,
    onError = androidx.compose.ui.graphics.Color.White,
    errorContainer = ErrorContainer,
    onErrorContainer = Error,
    
    outline = Border,
    outlineVariant = BorderLight
)

@Composable
fun FormularioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
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
