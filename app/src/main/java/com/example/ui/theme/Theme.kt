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

private val DarkColorScheme =
  darkColorScheme(
    primary = EduTealPrimaryDark,
    onPrimary = Color(0xFF00373C),
    primaryContainer = EduTealOnContainer,
    onPrimaryContainer = EduTealContainer,
    secondary = EduAmberSecondary,
    tertiary = EduBlueTertiary,
    background = Color(0xFF101415),
    surface = Color(0xFF191C1D),
    surfaceVariant = Color(0xFF282F30),
    onBackground = Color(0xFFE1E3E4),
    onSurface = Color(0xFFE1E3E4),
  )

private val LightColorScheme =
  lightColorScheme(
    primary = EduTealPrimary,
    onPrimary = Color.White,
    primaryContainer = EduTealContainer,
    onPrimaryContainer = EduTealOnContainer,
    secondary = EduAmberSecondary,
    onSecondary = Color.White,
    secondaryContainer = EduAmberContainer,
    onSecondaryContainer = EduAmberOnContainer,
    tertiary = EduBlueTertiary,
    tertiaryContainer = EduBlueContainer,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = DividerColor,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our intentional custom medical palette
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

