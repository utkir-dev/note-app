package com.example.mynotes.presentation.utils.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.mynotes.presentation.utils.components.image.customColors

@Composable
fun ItemEmptyRow(
    onItemClicked: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(color = MaterialTheme.customColors.backgroundItem)
            .border(
                width = 2.dp,
                color = MaterialTheme.customColors.borderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                //  onItemClicked()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        content = content
    )
}