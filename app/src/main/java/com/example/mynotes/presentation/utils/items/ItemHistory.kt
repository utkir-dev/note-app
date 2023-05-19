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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.Type
import com.example.common.getTypeText
import com.example.mynotes.models.HistoryItem
import com.example.mynotes.models.TypeHistory
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.Red
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.huminize
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun ItemHistory(
    item: HistoryItem,
    isCommentVisible: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    horizontalPading: Dp = 10.dp,
    verticalPading: Dp = 2.dp,
    background: Color = MaterialTheme.customColors.backgroundItem,
    onItemClicked: () -> Unit,
    //  content: @Composable RowScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalPading, horizontal = horizontalPading)
            .clip(RoundedCornerShape(6.dp))
            .background(color = background)
            .border(
                width = 1.dp,
                color = MaterialTheme.customColors.borderColor,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable {
                onItemClicked()
            }
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            var incr = ""
            var color = MaterialTheme.customColors.textColor
            if (item.title == getTypeText(Type.INCOME) || item.title == getTypeText(Type.CREDIT)) {
                incr = "+"
                color = Green
            } else if (item.title == getTypeText(Type.OUTCOME) || item.title == getTypeText(Type.DEBET)) {
                incr = "-"
                color = Red
            }
            MyText(
                text = item.title,
                fontSize = 16.sp,
                color = MaterialTheme.customColors.textColor,
                fontWeight = FontWeight.Bold
            )
            MyText(
                text = "$incr${item.amount.huminize()} ${item.currency}",
                color = color
            )
        }
        if (item.from.isNotEmpty()) {
            MyText(
                text = item.from,
                color = MaterialTheme.customColors.textColor,
            )
        }
        if (item.to.isNotEmpty()) {
            MyText(
                text = item.to,
                color = MaterialTheme.customColors.textColor,
            )
        }
        if (isCommentVisible && item.comment.isNotEmpty()) {
            MyText(
                text = "izoh: ${item.comment}",
                fontSize = 12.sp,
                color = MaterialTheme.customColors.subTextColor,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            MyText(
                text = item.date.huminize(),
                fontSize = 12.sp,
                color = MaterialTheme.customColors.subTextColor,
            )
        }
    }
}