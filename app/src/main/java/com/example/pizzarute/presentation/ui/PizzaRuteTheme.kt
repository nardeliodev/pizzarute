package com.example.pizzarute.presentation.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PizzaRutePrimary = Color(0xFFC8102E)
private val PizzaRutePrimaryDark = Color(0xFF8E0F20)
private val PizzaRutePrimaryContainer = Color(0xFFFFD9DD)
private val PizzaRuteOnPrimary = Color(0xFFFFFFFF)
private val PizzaRuteBackground = Color(0xFFFFF8F7)
private val PizzaRuteSurface = Color(0xFFFFFFFF)
private val PizzaRuteSurfaceVariant = Color(0xFFF8EDEE)

private val PizzaRuteDarkBackground = Color(0xFF1A1113)
private val PizzaRuteDarkSurface = Color(0xFF24181A)
private val PizzaRuteDarkSurfaceVariant = Color(0xFF382629)

@Composable
fun PizzaRuteTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = if (isSystemInDarkTheme()) {
        darkColorScheme(
            primary = PizzaRutePrimary,
            onPrimary = PizzaRuteOnPrimary,
            primaryContainer = PizzaRuteDarkSurfaceVariant,
            onPrimaryContainer = PizzaRuteOnPrimary,
            secondary = Color(0xFFFF8A8A),
            onSecondary = Color(0xFF3C0A10),
            tertiary = Color(0xFFF28B82),
            onTertiary = Color(0xFF3C0A10),
            background = PizzaRuteDarkBackground,
            onBackground = Color(0xFFFFEDEE),
            surface = PizzaRuteDarkSurface,
            onSurface = Color(0xFFFFEDEE),
            surfaceVariant = PizzaRuteDarkSurfaceVariant,
            onSurfaceVariant = Color(0xFFFFCFD2)
        )
    } else {
        lightColorScheme(
            primary = PizzaRutePrimary,
            onPrimary = PizzaRuteOnPrimary,
            primaryContainer = PizzaRutePrimaryContainer,
            onPrimaryContainer = PizzaRutePrimaryDark,
            secondary = Color(0xFFA5162A),
            onSecondary = PizzaRuteOnPrimary,
            tertiary = Color(0xFFF28B82),
            onTertiary = Color(0xFF5A1D22),
            background = PizzaRuteBackground,
            onBackground = Color(0xFF251014),
            surface = PizzaRuteSurface,
            onSurface = Color(0xFF251014),
            surfaceVariant = PizzaRuteSurfaceVariant,
            onSurfaceVariant = Color(0xFF6B4B4F)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
