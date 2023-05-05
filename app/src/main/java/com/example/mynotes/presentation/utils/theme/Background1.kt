package com.example.mynotes.presentation.utils.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import com.example.mynotes.presentation.utils.components.image.Blue_Light
import com.example.mynotes.presentation.utils.components.image.Blue_Middle
import com.example.mynotes.presentation.utils.components.image.Blue_dark

@Composable
fun background1(): Brush {
    return Brush.verticalGradient(
        colors = listOf(Blue_Light, Blue_Light, Blue_dark)
    )
}


