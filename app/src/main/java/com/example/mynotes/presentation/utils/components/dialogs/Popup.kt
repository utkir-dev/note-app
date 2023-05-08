package com.example.mynotes.presentation.utils.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText

@Composable
fun PopupDialog(text: String, offset: Offset) {
    val popupWidth = 180.dp
    val popupHeight = 150.dp
    val pxValue = LocalDensity.current.run { popupWidth.toPx() }
    Popup(
        offset = IntOffset((offset.x - pxValue).toInt(), offset.y.toInt()),
        // alignment = Alignment.CenterEnd,
        properties = PopupProperties()
    ) {
        Box(
            Modifier
                .size(popupWidth, popupHeight)
                .padding(top = 5.dp)
                .background(MaterialTheme.customColors.backgroundDialog, RoundedCornerShape(10.dp))
                .border(
                    1.dp,
                    color = MaterialTheme.customColors.borderColor,
                    RoundedCornerShape(10.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                MyText(
                    text = text,
                    color = MaterialTheme.customColors.textColor,
                    modifier = Modifier.padding(vertical = 5.dp),
                    fontSize = 16.sp
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.customColors.borderColor)
                )
                MyText(

                    text = "O'zgartirish",
                    color = MaterialTheme.customColors.textColor,
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .clickable {

                        },
                    fontSize = 16.sp
                )
                MyText(
                    text = "O'chirish",
                    color = MaterialTheme.customColors.textColor,
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .clickable {

                        },
                    fontSize = 16.sp
                )

            }
        }
    }
}