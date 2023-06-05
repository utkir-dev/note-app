package com.example.mynotes.presentation.utils.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.Type
import com.example.common.getTypeEnum
import com.example.common.getTypeNumber
import com.example.common.getTypeText
import com.example.mynotes.R
import com.example.mynotes.domain.models.HistoryDomain
import com.example.mynotes.presentation.utils.components.buttons.IconAnimationButton
import com.example.mynotes.presentation.utils.components.image.Green
import com.example.mynotes.presentation.utils.components.image.Red
import com.example.mynotes.presentation.utils.components.image.RedDark
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.huminize

@Composable
fun ItemHistory(
    item: HistoryDomain,
    horizontalPading: Dp = 10.dp,
    verticalPading: Dp = 2.dp,
    background: Color = MaterialTheme.customColors.backgroundItem,
    onIconClicked: () -> Unit
) {
    var visibilityComment by remember {
        mutableStateOf(false)
    }
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
                visibilityComment = !visibilityComment
            }
            .padding(5.dp)
    ) {

        var incr = ""
        var color = MaterialTheme.customColors.textColor
        if (item.title == getTypeNumber(Type.INCOME) || item.title == getTypeNumber(Type.CREDIT)) {
            incr = "+"
            color = Green
        } else if (item.title == getTypeNumber(Type.OUTCOME) || item.title == getTypeNumber(Type.DEBET)) {
            incr = "-"
            color = Red
        }
        val idPerson = R.drawable.ic_person
        val idPocket = R.drawable.ic_wallet

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MyText(
                text = getTypeText(item.title),
                fontSize = 16.sp,
                color = MaterialTheme.customColors.textColor,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (!item.uploaded) {
                IconAnimationButton(
                    imageId = R.drawable.ic_alert,
                    tint = RedDark
                ) {
                    onIconClicked()
                }
            }
            MyText(
                text = "$incr${item.amount.huminize()} ${item.currency}",
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
        if (!item.fromName.isNullOrEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = if (item.isFromPocket) idPocket else idPerson),
                    contentDescription = "person",
                    tint = MaterialTheme.customColors.subTextColor
                )
                val pocket = if (item.isFromPocket) " hamyon" else ""
                MyText(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    text = (item.fromName ?: "") + "${pocket}dan",
                    color = MaterialTheme.customColors.textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (getTypeEnum(item.title) == Type.CONVERTATION) {
                    MyText(
                        text = "-${item.moneyFrom?.huminize()} ${item.moneyNameFrom}",
                        color = Red,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }

            }
        }
        if (!item.toName.isNullOrEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = if (item.isToPocket) idPocket else idPerson),
                    contentDescription = "person",
                    tint = MaterialTheme.customColors.subTextColor
                )
                val pocket = if (item.isToPocket) " hamyon" else ""
                MyText(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    text = (item.toName ?: "") + "${pocket}ga",
                    color = MaterialTheme.customColors.textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (getTypeEnum(item.title) == Type.CONVERTATION) {
                    MyText(
                        text = "+${item.moneyTo?.huminize()} ${item.moneyNameTo}",
                        color = Green,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            val kurs =
                if (item.rateFrom > item.rateTo) {
                    "1 ${item.moneyNameTo} = ${(item.rateFrom / item.rateTo).huminize()} ${item.moneyNameFrom}"
                } else if (item.rateFrom < item.rateTo) {
                    "1 ${item.moneyNameFrom} = ${(item.rateTo / item.rateFrom).huminize()} ${item.moneyNameTo}"
                } else if (item.rateFrom != 1.0 && item.rateTo != 1.0) {
                    "1$ = ${item.rateTo.huminize()}"
                } else " "

            MyText(
                text = kurs,
                fontSize = 12.sp,
                color = MaterialTheme.customColors.subTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            MyText(
                text = item.date.huminize(),
                fontSize = 12.sp,
                color = MaterialTheme.customColors.subTextColor,
            )
        }
        if (visibilityComment) {
            var izoh = ""
            item.comment?.let {
                if (it.isNotEmpty()) {
                    izoh = "\nizoh: $it"
                }
            }
            MyText(
                text = "oldingi balans: ${item.balance} $$izoh",
                color = MaterialTheme.customColors.subTextColor,
                fontSize = 12.sp
            )
        }

    }
}

