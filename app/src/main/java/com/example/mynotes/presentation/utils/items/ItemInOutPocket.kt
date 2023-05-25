package com.example.mynotes.presentation.utils.items


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.R
import com.example.mynotes.domain.models.WalletOwnerDomain
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.huminize

@Composable
fun ItemInOutPocket(
    text: String,
    chipsVisibility: Boolean = false,
    chips: List<WalletOwnerDomain> = emptyList(),
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
                    .padding(bottom = 3.dp),
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
                            Chip(text = "${wallet.currencyBalance.huminize()} ${wallet.currencyName}")
                        }
                }
            }

        }
    }
}
