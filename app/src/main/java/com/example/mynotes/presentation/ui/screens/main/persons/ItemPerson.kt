package com.example.mynotes.presentation.utils.items


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.R
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.domain.models.WalletOwnerDomain
import com.example.mynotes.presentation.utils.components.buttons.IconAnimationButton
import com.example.mynotes.presentation.utils.components.image.RedDark
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.huminize

@Composable
fun ItemPerson(
    person: PersonDomain,
    chips: List<WalletOwnerDomain> = emptyList(),
    color: Color = MaterialTheme.customColors.textColor,
    onItemClicked: () -> Unit,
    onIconClicked: () -> Unit,
    onMenuMoreClicked: (Offset) -> Unit,
) {

    var offset by remember {
        mutableStateOf(Offset.Infinite)
    }
    ItemEmptyRow(
        verticalPading = 2.dp, onItemClicked = {
            onItemClicked()

        }) {
        Column(modifier = Modifier.padding(3.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MyText(
                    text = person.name,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!person.uploaded) {
                    IconAnimationButton(
                        imageId = R.drawable.ic_alert,
                        tint = RedDark
                    ) {
                        onIconClicked()
                    }
                }
                Spacer(modifier = Modifier.weight(1.0f))
                IconButton(
                    modifier = Modifier
                        .onGloballyPositioned { layoutCoordinates ->
                            val rect = layoutCoordinates.boundsInRoot()
                            offset = rect.bottomRight
                        }
                        .padding(start = 3.dp),
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
            if (chips.isNotEmpty()) {
                ChipVerticalGrid(
                    spacing = 2.dp,
                    modifier = Modifier
                        .background(MaterialTheme.customColors.backgroundItem)
                ) {
                    chips.forEach { wallet ->
                        Chip(text = "${wallet.currencyBalance.huminize()} ${wallet.currencyName}")
                    }
                }
            }

        }
    }
}
