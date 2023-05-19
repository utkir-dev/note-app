package com.example.mynotes.presentation.ui.screens.main.currencies


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.DialogCurrency
import com.example.mynotes.presentation.utils.components.buttons.ButtonAdd
import com.example.mynotes.presentation.utils.components.dialogs.DialogConfirm
import com.example.mynotes.presentation.utils.components.dialogs.PopupDialog
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.items.ItemCurrency
import com.example.mynotes.presentation.utils.types.PopupType

class CurrencyScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: CurrencyViewModelImp = getViewModel()
        ShowCurrencies(viewModel)
    }
}

@Composable
fun ShowCurrencies(
    viewModel: CurrencyViewModelImp

) {
    val listCurrency by viewModel.currencies.collectAsStateWithLifecycle(emptyList())

    var visibilityPopup by remember {
        mutableStateOf(false)
    }
    var offsetPopup by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    var visibilityDialog by remember {
        mutableStateOf(false)
    }
    var visibilityConfirm by remember {
        mutableStateOf(false)
    }
    var currency by remember {
        mutableStateOf(CurrencyDomain(""))
    }

    if (visibilityPopup) {
        PopupDialog(currency.name, offsetPopup) { type ->
            when (type) {
                PopupType.EDIT -> {
                    visibilityDialog = true
                }
                PopupType.DELETE -> {
                    visibilityConfirm = true
                }
                PopupType.CANCEL -> {
                    visibilityPopup = false
                }
            }
            visibilityPopup = false
        }
    }
    if (visibilityDialog) {
        visibilityPopup = false
        DialogCurrency(currency) { cur ->
            cur?.let {
                currency = it
                if (currency.isValid()) {
                    // notes.add(currency)
                    viewModel.add(currency)
                }
            }
            visibilityDialog = false
        }
    }
    if (visibilityConfirm) {
        visibilityPopup = false
        DialogConfirm(clazz = currency) { boo, clazz ->
            val cur = clazz as CurrencyDomain
            if (boo && cur.isValid()) {
                // notes.remove(currency)
                viewModel.delete(cur)
            }
            visibilityConfirm = false
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput("screenToutch") {
                detectTapGestures(onPress = {
                    visibilityPopup = false
                }, onTap = {
                    visibilityPopup = false
                })
            }
            .scrollable(
                orientation = Orientation.Vertical,
                state = rememberScrollableState { delta ->
                    visibilityPopup = false
                    //offset += delta
                    delta
                })
            .background(brush = MaterialTheme.customColors.backgroundBrush)
    ) {
        item(key = R.drawable.ic_arrow_back) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    viewModel.back()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "back arrow",
                        tint = MaterialTheme.customColors.textColor
                    )
                }
                MyText(
                    modifier = Modifier.weight(1.0f),
                    text = "Valyutalar",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.customColors.textColor
                )
            }
        }
        items(items = listCurrency, key = {
            it.id
        }) { currencyDomain ->
            ItemCurrency(
                currency = currencyDomain,
                onItemClicked = {
                    visibilityPopup = false
                }, onMenuMoreClicked = { offset ->
                    currency = currencyDomain
                    offsetPopup = offset
                    visibilityPopup =
                        !visibilityPopup// if (currencyDomain == listCurrency[index]) !visibilityPopup else true

                })
        }

        item(key = "add new currency") {
            ButtonAdd(text = "Yangi valyuta qo'shish", onClicked = {
                currency = CurrencyDomain("")
                visibilityDialog = true
            })
        }
    }
}

