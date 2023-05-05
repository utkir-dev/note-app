package com.example.mynotes.presentation.utils.components.image

import android.app.Activity
import android.os.Build
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import com.example.mynotes.presentation.utils.theme.MyColors
import com.example.mynotes.presentation.utils.theme.ThemeState

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)
private val LocalCustomColors = staticCompositionLocalOf { MyColors() }

private val LightCustomColors = MyColors(
    textColor = Color(0xFF000000),
    subTextColor = Color(0xFF696969),
    backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFD9D9D9), Color(0xFFD9D9D9))
    ),
    backgroundItem = Color(0xFFFFFFFF),
    backgroundEditText = Color(0xFFD9D9D9),
    borderColor = Color(0xFF948888),
    errorText = Color(0xFFF60808),
    confirmButtonColor = Color(0xFF0BCB83),
    deleteButtonColor = Color(0xFFF60808),
    cancelButtonColor = Color(0xFF9C9C9C),
    iconColor = Color(0xFF858585)
)

private val DarkCustomColors = MyColors(
    textColor = Color(0xFFFFFFFF),
    subTextColor = Color(0xFFE4E4E4),
    backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4F7FA3),
            Color(0xFF4F7FA3),
            Color(0xFF333940)
        )
    ),
    backgroundItem = Color(0xFF405C72),
    backgroundEditText = Color(0xFFD9D9D9),
    borderColor = Color(0xFF807575),
    errorText = Color(0xFFF60808),
    confirmButtonColor = Color(0xFF0BCB83),
    deleteButtonColor = Color(0xFFF60808),
    cancelButtonColor = Color(0xFF9C9C9C),
    iconColor = Color(0xFFC7C7C7)
)

@Composable
fun MyNotesTheme(
    darkTheme: Boolean,
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

    val customColors =
        if (darkTheme) DarkCustomColors
        else LightCustomColors
    CompositionLocalProvider(
        LocalCustomColors provides customColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }
}

val MaterialTheme.customColors: MyColors
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColors.current