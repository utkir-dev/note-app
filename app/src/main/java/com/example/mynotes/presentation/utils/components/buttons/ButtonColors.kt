package com.example.mynotes.presentation.utils.components.buttons

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import com.example.mynotes.presentation.utils.components.image.customColors

@Composable
fun buttonColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = MaterialTheme.customColors.textColor,
    focusedBorderColor = MaterialTheme.customColors.subTextColor,
    focusedLabelColor = MaterialTheme.customColors.subTextColor,
    unfocusedBorderColor = MaterialTheme.customColors.subTextColor,
    cursorColor = MaterialTheme.customColors.textColor
)
