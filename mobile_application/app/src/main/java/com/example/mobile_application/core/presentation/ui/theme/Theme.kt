package com.example.mobile_application.core.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// Define your custom colors
private val DarkColorPalette = darkColorScheme(
    primary = YellowMain,
    onPrimary = MainWhiteColor,
    secondary = GrayColor,
    background = Color.Black,
    surface = DarkBlue,
    onSurface = MainWhiteColor
)

private val LightColorPalette = lightColorScheme(
    primary = DarkBlue,
    onPrimary = Color.White,
    secondary = GrayColor,
    background = Color.White,
    surface = MainWhiteColor,
    onSurface = DarkBlue
)

@Composable
fun CustomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()

    // Side effect for system UI controller (status bar color change)
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = if (darkTheme) DarkBlue else MainWhiteColor
        )
    }

    // Apply the Material 3 theme
    MaterialTheme(
        colorScheme = colorScheme, // Material 3 uses colorScheme instead of colors
        typography = Typography,  // Default Material 3 typography
        shapes = Shapes,
        content = content
    )
}
