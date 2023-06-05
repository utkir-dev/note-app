package com.example.mynotes.presentation.utils.items


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.R
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.presentation.utils.components.buttons.IconAnimationButton
import com.example.mynotes.presentation.utils.components.image.RedDark
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText

@Composable
fun ItemCurrency(
    currency: CurrencyDomain,
    color: Color = MaterialTheme.customColors.textColor,
    onIconClicked: () -> Unit,
    onMenuMoreClicked: (Offset) -> Unit,
) {
    var offset by remember {
        mutableStateOf(Offset.Infinite)
    }
    ItemEmptyRow(onItemClicked = {

    }) {
//        Icon(
//            modifier = Modifier.padding(horizontal = 5.dp),
//            imageVector = Icons.Default.Person,// painterResource(id = iconId),
//            contentDescription = "item common",
//            tint = color
//        )
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Row() {
                MyText(
                    text = currency.name,
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!currency.uploaded) {
                    IconAnimationButton(
                        imageId = R.drawable.ic_alert,
                        tint = RedDark
                    ) {
                        onIconClicked()
                    }
                }
            }

            MyText(
                text = "1$ = ${currency.rate}",
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                color = color
            )
        }

        Spacer(modifier = Modifier.weight(1.0f))
        IconButton(
            modifier = Modifier
                .onGloballyPositioned { layoutCoordinates ->
                    val rect = layoutCoordinates.boundsInRoot()
                    offset = rect.bottomRight
                }
                .padding(start = 5.dp),
            onClick = {
                onMenuMoreClicked(offset)
            }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_more), //imageVector = Icons.Filled.Menu,
                contentDescription = "menu more",
                tint = color
            )
        }
    }
}
