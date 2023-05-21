package com.example.mynotes.presentation.ui.screens.main.getcredit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.models.ModelDomain
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText

@Composable
fun PopupGetCredit(
    persons: List<ModelDomain>,
    viewModel: GetCreditViewModel,
    offset: Offset,
    onSelected: () -> Unit
) {
    val popupWidth = 200.dp
    val pxValue = LocalDensity.current.run { popupWidth.toPx() }
    val pxHeight = LocalDensity.current.run { 50.dp.toPx() }
    Popup(
        offset = IntOffset((offset.x - pxValue).toInt(), (offset.y - pxHeight).toInt()),
        properties = PopupProperties()
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(6.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(
                    MaterialTheme.customColors.backgroundDialog,
                    RoundedCornerShape(10.dp)
                )
                .border(
                    2.dp,
                    color = MaterialTheme.customColors.borderColor,
                    RoundedCornerShape(10.dp)
                )
                .heightIn(max = 600.dp)
                .width(200.dp)
                .padding(6.dp)
        ) {
            items(items = persons, key = { p ->
                p.hashCode()
            }) { xDomain ->
                MyText(text = xDomain.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when (xDomain) {
                                is PersonDomain -> viewModel.setPerson(xDomain)
                                is PocketDomain -> viewModel.setPocket(xDomain)
                                is CurrencyDomain -> viewModel.setCurrency(xDomain)
                            }
                            onSelected()
                        }
                        .padding(5.dp),
                    color = MaterialTheme.customColors.textColor)
            }
        }
    }
}