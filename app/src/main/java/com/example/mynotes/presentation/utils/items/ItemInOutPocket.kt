package com.example.mynotes.presentation.utils.items


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.R
import com.example.mynotes.models.WalletItem
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.huminize

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemInOutPocket(
    text: String,
    chipsVisibility: Boolean = false,
    chips: List<WalletItem> = emptyList(),
    color: Color = MaterialTheme.customColors.textColor,
    onItemClicked: () -> Unit,
    iconId: Int = R.drawable.ic_person,
    iconStartVisibility: Boolean = false,
    iconEndVisibility: Boolean = false,
    directionType: DirectionType = DirectionType.BALANCE,
) {
    ItemEmptyRow(onItemClicked = {
        onItemClicked()
    }) {
//        Icon(
//            modifier = Modifier.padding(horizontal = 5.dp),
//            painter = painterResource(id = iconId),
//            contentDescription = "item common",
//            tint = color
//        )

        Column(modifier = Modifier.padding(5.dp)) {
            MyText(
                text = text,
                modifier = Modifier
                    .padding(bottom = 3.dp)
                    .basicMarquee(),
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                color = color,
                fontWeight = FontWeight.SemiBold
            )

            if (chipsVisibility) {
                ChipVerticalGrid(
                    spacing = 2.dp,
                    modifier = Modifier
                        .background(MaterialTheme.customColors.backgroundItem)
                ) {
                    if (chips.isEmpty()) {
                        Chip(text = "hamyonda pul yo'q")
                    } else
                        chips.forEach { wallet ->
                            if (wallet.balance >= 0.01)
                                Chip(text = "${wallet.balance.huminize()} ${wallet.currencyName}")
                        }
                }
            }

        }
    }
}
