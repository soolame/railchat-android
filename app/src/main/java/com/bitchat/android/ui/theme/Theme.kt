package com.bitchat.android.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

// Standard UI semantics live in Material so stock components and custom Bitchat composables
// share one source of truth. LocalBitchatPalette below only supplies app-specific extra colors.
internal val DarkBitchatColorScheme = darkColorScheme(
    primary = Color(0xFF4C86FF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF12294F),
    onPrimaryContainer = Color(0xFFC7DBFF),
    secondary = Color(0xFFFF5449),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF5C130D),
    onSecondaryContainer = Color(0xFFFFDAD4),
    tertiary = DarkBitchatPalette.accentOrange,
    onTertiary = Color.Black,
    background = Color(0xFF000000),
    onBackground = Color(0xFFF5F5F5),
    surface = Color(0xFF0E1420),
    onSurface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFF182030),
    onSurfaceVariant = Color(0xFF9AA6B6),
    outline = Color(0xFF2A3550),
    outlineVariant = Color(0xFF1C2438),
    error = Color(0xFFFF453A),
    onError = Color.Black
)

internal val LightBitchatColorScheme = lightColorScheme(
    primary = Color(0xFF1C3F94),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD9E4FF),
    onPrimaryContainer = Color(0xFF001A43),
    secondary = Color(0xFFC6291A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDAD4),
    onSecondaryContainer = Color(0xFF3B0906),
    tertiary = LightBitchatPalette.accentOrange,
    onTertiary = Color.Black,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF131A1F),
    surface = Color(0xFFF2F4F8),
    onSurface = Color(0xFF131A1F),
    surfaceVariant = Color(0xFFE7EAEE),
    onSurfaceVariant = Color(0xFF4C5560),
    outline = Color(0xFFCBD2DE),
    outlineVariant = Color(0xFFDEE2E8),
    error = Color(0xFFD70015),
    onError = Color.White
)

@Composable
fun BitchatTheme(
    darkTheme: Boolean? = null,
    content: @Composable () -> Unit
) {
    // App-level override from ThemePreferenceManager
    val themePref by ThemePreferenceManager.themeFlow.collectAsState(initial = ThemePreference.System)
    val shouldUseDark = when (darkTheme) {
        true -> true
        false -> false
        null -> when (themePref) {
            ThemePreference.Dark -> true
            ThemePreference.Light -> false
            ThemePreference.System -> isSystemInDarkTheme()
        }
    }

    val colorScheme = if (shouldUseDark) DarkBitchatColorScheme else LightBitchatColorScheme
    val palette = if (shouldUseDark) DarkBitchatPalette else LightBitchatPalette

    val view = LocalView.current
    SideEffect {
        (view.context as? Activity)?.window?.let { window ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(
                    if (!shouldUseDark) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = if (!shouldUseDark) {
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else 0
            }
            window.navigationBarColor = colorScheme.background.toArgb()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
        }
    }

    CompositionLocalProvider(LocalBitchatPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
