package com.example.mynotes.presentation.utils.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Immutable
data class MyColors(
    val textColor: Color = Color.Unspecified,
    val subTextColor: Color = Color.Unspecified,
    val backgroundBrush: Brush = Brush.verticalGradient(
        colors = listOf(Color(0xFFD9D9D9), Color(0xFFD9D9D9))
    ),
    val backgroundItem: Color = Color.Unspecified,
    val backgroundEditText: Color = Color.Unspecified,
    val borderColor: Color = Color.Unspecified,
    val errorText: Color = Color.Unspecified,
    val confirmButtonColor: Color = Color.Unspecified,
    val deleteButtonColor: Color = Color.Unspecified,
    val cancelButtonColor: Color = Color.Unspecified,
    val iconColor: Color = Color.Unspecified,
)

