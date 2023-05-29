package com.example.mynotes.presentation.utils.items


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.R
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.models.WalletOwnerDomain
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.huminize

@Composable
fun ItemInPocket(
    pocket: PocketDomain,
    chipsVisibility: Boolean = false,
    chips: List<WalletOwnerDomain> = emptyList(),
    color: Color = MaterialTheme.customColors.textColor,
    onItemClicked: () -> Unit,
    onMenuMoreClicked: (Offset) -> Unit,
    iconStartVisibility: Boolean = false,
    iconEndVisibility: Boolean = false,
    directionType: DirectionType = DirectionType.BALANCE,
) {
    var offset by remember {
        mutableStateOf(Offset.Infinite)
    }
    ItemEmptyRow(onItemClicked = {
        onItemClicked()
    }) {
        Column(modifier = Modifier.padding(5.dp)) {
            MyText(
                text = pocket.name,
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
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "menu more",
                tint = color
            )
        }
    }
}
