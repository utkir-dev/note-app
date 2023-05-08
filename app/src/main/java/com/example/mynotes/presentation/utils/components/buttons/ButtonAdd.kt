package com.example.mynotes.presentation.utils.components.buttons

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.R
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.utils.components.image.GreenPlus
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText

@Composable
fun ButtonAdd(
    text: String,
    color: Color = GreenPlus,
    onClicked: () -> Unit,
    iconId: Int = R.drawable.ic_add_circle,
    iconStartVisibility: Boolean = false,
    iconEndVisibility: Boolean = false,
    directionType: DirectionType = DirectionType.BALANCE,
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
                onClicked()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = Modifier.padding(horizontal = 5.dp),
            painter = painterResource(id = iconId),
            contentDescription = "item common",
            tint = color
        )
        MyText(
            text = text,
            modifier = Modifier
                .weight(1.0f)
                .padding(5.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = color
        )
    }

}
