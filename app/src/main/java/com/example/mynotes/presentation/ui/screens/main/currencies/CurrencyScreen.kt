package com.example.mynotes.presentation.ui.screens.main.currencies


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
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
import com.example.mynotes.presentation.utils.components.dialogs.DialogAttention
import com.example.mynotes.presentation.utils.components.dialogs.DialogConfirm
import com.example.mynotes.presentation.utils.components.dialogs.PopupDialog
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.items.ItemCurrency
import com.example.mynotes.presentation.utils.types.PopupType
import com.library.utils.isInternetAvailable
import kotlinx.coroutines.runBlocking

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
    val wallets by viewModel.wallets.collectAsStateWithLifecycle(emptyList())

    var visibilityPopup by remember {
        mutableStateOf(false)
    }
    var visibilityAlert by remember {
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


    if (visibilityDialog) {
        visibilityPopup = false
        DialogCurrency(viewModel, listCurrency) {
            visibilityDialog = false
        }
    }
    var alertMessage by remember {
        mutableStateOf("")
    }

    if (visibilityAlert) {
        DialogAttention(alertMessage) {
            visibilityAlert = false
        }
    }
    if (visibilityPopup) {
        PopupDialog(viewModel.getCurrency().name, offsetPopup) { type ->
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

    if (visibilityConfirm) {
        visibilityPopup = false
        DialogConfirm(clazz = viewModel.getCurrency(), onDismiss = {
            visibilityConfirm = false
        }) { boo ->
            if (boo) {
                if (wallets.filter { it.currencyId == viewModel.getCurrency().id }.size > 0) {
                    alertMessage = "Bu valyuta orqali qilingan ishlar bor. O'chirish mumkin emas !"
                    visibilityAlert = true
                } else if (viewModel.getCurrency().id == "dollar") {
                    alertMessage = "Dollarni o'chirish mumkin emas !"
                    visibilityAlert = true
                } else {
                    viewModel.delete()
                }

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
                    modifier = Modifier
                        .weight(1.0f)
                        .padding(start = 8.dp),
                    text = "Valyutalar",
                    fontSize = 18.sp,
                    color = MaterialTheme.customColors.textColor
                )
            }
        }
        items(items = listCurrency, key = {
            it.id
        }) { currencyDomain ->
            ItemCurrency(
                currency = currencyDomain,
                onMenuMoreClicked = { offset ->
                    viewModel.currency.value = currencyDomain
                    offsetPopup = offset
                    visibilityPopup =
                        !visibilityPopup// if (currencyDomain == listCurrency[index]) !visibilityPopup else true
                },
                onIconClicked = {
                    alertMessage =
                        "Bu ma'lumot serverga saqlanmagan. Internetni yo'qib qaytadan bosing"
                    visibilityAlert = true
                    if (!currencyDomain.uploaded) {
                        viewModel.add(currencyDomain)
                    }
                    visibilityPopup = false
                }
            )
        }

        item(key = "add new currency") {
            ButtonAdd(text = "Yangi valyuta qo'shish", onClicked = {
                viewModel.currency.value = CurrencyDomain("")
                visibilityDialog = true
            })
        }
    }
}

