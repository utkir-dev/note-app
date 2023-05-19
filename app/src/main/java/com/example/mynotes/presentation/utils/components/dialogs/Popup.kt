package com.example.mynotes.presentation.utils.components.dialogs

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.mynotes.R
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.types.PopupType

@Composable
fun PopupDialog(text: String, offset: Offset, onSelected: (PopupType) -> Unit) {
    val popupWidth = 220.dp
    val popupHeight = 230.dp
    val pxValue = LocalDensity.current.run { popupWidth.toPx() }
    var visible by remember { mutableStateOf(true) }
    val density = LocalDensity.current

    Popup(
        offset = IntOffset((offset.x - pxValue).toInt(), offset.y.toInt()),
        // alignment = Alignment.CenterEnd,
        properties = PopupProperties()
    ) {

        Box(
            Modifier
                .size(popupWidth, popupHeight)
                .padding(5.dp)
                .background(
                    MaterialTheme.customColors.backgroundDialog,
                    RoundedCornerShape(10.dp)
                )
                .border(
                    2.dp,
                    color = MaterialTheme.customColors.borderColor,
                    RoundedCornerShape(10.dp)
                )
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .size(popupWidth, popupHeight)
                    .padding(5.dp)
                    .background(
                        MaterialTheme.customColors.backgroundDialog,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MyText(
                    text = text,
                    color = MaterialTheme.customColors.textColor,
                    modifier = Modifier
                        .padding(7.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .height(2.dp)
                        .background(MaterialTheme.customColors.borderColor)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(10.dp))
                        .clickable {
                            onSelected(PopupType.EDIT)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "edit icon",
                        tint = MaterialTheme.customColors.iconColor
                    )
                    MyText(
                        text = "O'zgartirish",
                        color = MaterialTheme.customColors.textColor,
                        modifier = Modifier
                            .padding(7.dp),
                        fontSize = 16.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(10.dp))
                        .clickable {
                            onSelected(PopupType.DELETE)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "edit icon",
                        tint = MaterialTheme.customColors.iconColor
                    )
                    MyText(
                        text = "O'chirish",
                        color = MaterialTheme.customColors.textColor,
                        modifier = Modifier.padding(7.dp),
                        fontSize = 16.sp
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    MyText(
                        modifier = Modifier
                            .clickable {
                                onSelected(PopupType.CANCEL)
                            }
                            .padding(horizontal = 3.dp),
                        text = "Bekor",
                        color = MaterialTheme.customColors.subTextColor,
                        fontWeight = FontWeight.Medium
                    )
                }


            }
        }
    }
}