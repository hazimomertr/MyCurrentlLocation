package edu.trakya.hazimomertr.currentlocation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val DarkColorPalette = darkColorScheme(
    primary = Color(0xFFfdfdfd), // Replace with your desired primary color
    background = Color(0xFF050606) // Replace with your desired background color
)

val LightColorPalette = lightColorScheme(
    primary = Color(0xFFfdfdfd), // Replace with your desired primary color
    background = Color(0xFF050606) // Replace with your desired background color
)

@Composable
fun CurrentLocationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}