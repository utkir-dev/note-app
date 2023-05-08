package com.example.mynotes.presentation.utils.items


import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.R
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText

@Composable
fun ItemCommon(
    text: String,
    color: Color = MaterialTheme.customColors.textColor,
    onItemClicked: () -> Unit,
    onMenuMoreClicked: (Offset) -> Unit,
    iconId: Int = R.drawable.ic_person,
    iconStartVisibility: Boolean = false,
    iconEndVisibility: Boolean = false,
    directionType: DirectionType = DirectionType.BALANCE,
) {
    var offset by remember {
        mutableStateOf(Offset.Infinite)
    }
    ItemEmptyRow(onItemClicked = {}) {
        Icon(
            modifier = Modifier.padding(horizontal = 5.dp),
            painter = painterResource(id = iconId),
            contentDescription = "item common",
            tint = color
        )
        MyText(

            text = text,
            modifier = Modifier
                .padding(5.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = color
        )
        Spacer(modifier = Modifier.weight(1.0f))
        IconButton(
            modifier = Modifier
                .onGloballyPositioned { layoutCoordinates ->
                    val rect = layoutCoordinates.boundsInRoot()
                    offset = rect.bottomRight
                    Log.d(
                        "offset",
                        "onGloballyPositioned : top= ${rect.topLeft}  bottom= ${rect.bottomRight}"
                    )

                }
                .padding(start = 5.dp),
            onClick = {
                Log.d("offset", "offset : x= ${offset.x}  y= ${offset.y}")
                onMenuMoreClicked(offset)
            }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "menu more",
                tint = color
            )
        }
    }

}
