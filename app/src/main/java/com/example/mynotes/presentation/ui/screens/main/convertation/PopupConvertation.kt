package com.example.mynotes.presentation.ui.screens.main.convertation

import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mynotes.domain.models.*
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.huminize

@Composable
fun PopupGetCredit(
    list: List<ModelDomain>,
    viewModel: ConvertationViewModelImp,
    type: ConvertType,
    offset: Offset,
    onSelected: () -> Unit
) {
    val durationUp: Int = 200
    val durationDown: Int = 100
    val scaleUp: Float = 1.02f
    val scaleDown: Float = 0.9f
    val scale = remember {
        androidx.compose.animation.core.Animatable(1f)
    }
    LaunchedEffect(key1 = scale) {
        scale.animateTo(
            scaleDown,
            animationSpec = tween(durationDown),
        )
        scale.animateTo(
            scaleUp,
            animationSpec = tween(durationUp),
        )
        scale.animateTo(
            1f,
            animationSpec = tween(durationDown),
        )
    }

    val currencyFrom by viewModel.currencyFrom.collectAsStateWithLifecycle()

    val popupWidth = 200.dp
    val pxValue = LocalDensity.current.run { popupWidth.toPx() }
    val pxHeight = LocalDensity.current.run { 50.dp.toPx() }
    Popup(
        offset = IntOffset((offset.x - pxValue).toInt(), (offset.y - pxHeight).toInt()),
        properties = PopupProperties()
    ) {
        LazyColumn(
            modifier = Modifier
                .scale(scale.value)
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
            items(items = list, key = { p ->
                p.hashCode()
            }) { xDomain ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        when (type) {
                            ConvertType.FROM_POCKET -> viewModel.setPocketFrom(xDomain as PocketDomain)
                            ConvertType.FROM_CURRENCY -> {
                                viewModel.setCurrencyFrom(xDomain as CurrencyDomain)
                                viewModel.setCurrency(xDomain)
                            }
                            ConvertType.TO_POCKET -> viewModel.setPocketTo(xDomain as PocketDomain)
                            ConvertType.TO_CURRENCY -> {
                                viewModel.setCurrencyTo(xDomain as CurrencyDomain)
                                viewModel.setCurrency(xDomain)
                            }
                            ConvertType.CURRENCY -> viewModel.setCurrency(xDomain as CurrencyDomain)
                        }
                        onSelected()
                    }) {

                    MyText(
                        text = xDomain.name,
                        modifier = Modifier
                            .fillMaxWidth()

                            .padding(top = 5.dp),
                        color = MaterialTheme.customColors.textColor
                    )

                    if (xDomain is PocketDomain) {
                        val wallet: WalletDomain? =
                            viewModel.wallets.collectAsStateWithLifecycle(emptyList()).value.filter {
                                it.ownerId == xDomain.id && it.currencyId == viewModel.currencyFrom.value.id
                            }.firstOrNull()

                        MyText(
                            text = if (wallet == null) "0 ${currencyFrom.name}" else
                                "${wallet.balance.huminize()} ${currencyFrom.name}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 5.dp),
                            fontSize = 12.sp,
                            color = MaterialTheme.customColors.subTextColor
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.customColors.borderColor)
                        )
                    }


                }

            }
        }
    }
}