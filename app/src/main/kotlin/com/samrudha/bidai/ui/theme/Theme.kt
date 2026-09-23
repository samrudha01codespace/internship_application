package com.samrudha.bidai.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary           = BluePrimary,
    onPrimary         = BlueOnPrimary,
    primaryContainer  = BlueContainer,
    onPrimaryContainer = BlueOnContainer,
    background        = White,
    onBackground      = OnSurfaceLight,
    surface           = White,
    onSurface         = OnSurfaceLight,
    onSurfaceVariant  = OnSurfaceMuted,
    secondary         = BluePrimary,
    tertiary          = BluePrimary,
    outline           = OutlineLight
)

@Composable
fun bidaiTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
