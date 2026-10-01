package com.example.ui.theme

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
    primary = ScholarPrimaryLight,
    onPrimary = ScholarPrimaryDark,
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = ScholarGoldLight,
    onSecondary = Color(0xFF452B00),
    secondaryContainer = Color(0xFF5E3C00),
    onSecondaryContainer = Color(0xFFFFD54F),
    tertiary = ScholarEmeraldLight,
    onTertiary = Color(0xFF003912),
    background = EduBackgroundDark,
    onBackground = EduTextPrimaryDark,
    surface = EduSurfaceDark,
    onSurface = EduTextPrimaryDark,
    surfaceVariant = EduSurfaceVariantDark,
    onSurfaceVariant = EduTextSecondaryDark,
    outline = Color(0xFF334155)
)

private val LightColorScheme = lightColorScheme(
    primary = ScholarPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = ScholarPrimaryDark,
    secondary = ScholarGoldDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF8E1),
    onSecondaryContainer = Color(0xFF6D3B00),
    tertiary = ScholarEmerald,
    onTertiary = Color.White,
    background = EduBackgroundLight,
    onBackground = EduTextPrimaryLight,
    surface = EduSurfaceLight,
    onSurface = EduTextPrimaryLight,
    surfaceVariant = EduSurfaceVariantLight,
    onSurfaceVariant = EduTextSecondaryLight,
    outline = Color(0xFFD0D5DD)
)

@Composable
fun SinhaJeeStudyCentreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to preserve our branded educational navy/gold colors
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
