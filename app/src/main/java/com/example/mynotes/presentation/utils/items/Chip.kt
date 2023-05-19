package com.example.mynotes.presentation.utils.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText

@Composable
fun Chip(
    text: String = "Chip",
) {
    Surface(
        modifier = Modifier
            .padding(2.dp)
            .background(color = MaterialTheme.customColors.backgroundItem),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.customColors.subTextColor)
    ) {
        Row {
            MyText(
                text = text,
                fontSize = 14.sp,
                color = MaterialTheme.customColors.subTextColor,
                modifier = Modifier
                    .background(color = MaterialTheme.customColors.backgroundItem)
                    .padding(horizontal = 4.dp)
            )
        }
    }
}