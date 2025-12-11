package com.example.proyectologin006d_final.ui.theme

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
    primary = Chocolate,
    secondary = RosaSuave,
    tertiary = MarronOscuro,
    background = MarronOscuro,
    surface = MarronOscuro,
    onPrimary = CremaPastel,
    onSecondary = MarronOscuro,
    onTertiary = CremaPastel,
    onBackground = CremaPastel,
    onSurface = CremaPastel
)

private val LightColorScheme = lightColorScheme(
    primary = Chocolate,
    secondary = RosaSuave,
    tertiary = MarronOscuro,
    background = CremaPastel,
    surface = CremaPastel,
    onPrimary = CremaPastel,
    onSecondary = MarronOscuro,
    onTertiary = MarronOscuro,
    onBackground = MarronOscuro,
    onSurface = MarronOscuro
)

@Composable
fun ProyectoLogin006D_finalTheme(
    darkTheme: Boolean = false, // Forzar tema claro por defecto
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Deshabilitado para evitar problemas de pantalla negra
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