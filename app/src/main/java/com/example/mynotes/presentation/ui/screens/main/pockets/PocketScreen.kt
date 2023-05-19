package com.example.mynotes.presentation.ui.screens.main.pockets


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.DialogPocket
import com.example.mynotes.presentation.utils.components.buttons.ButtonAdd
import com.example.mynotes.presentation.utils.components.dialogs.DialogConfirm
import com.example.mynotes.presentation.utils.components.dialogs.PopupDialog
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.items.ItemInPocket
import com.example.mynotes.presentation.utils.types.PopupType

class PocketScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: PocketViewModelImp = getViewModel()
        ShowCurrencies(viewModel)
    }
}

@Composable
fun ShowCurrencies(
    viewModel: PocketViewModelImp

) {
    val listPocket by viewModel.pockets.collectAsStateWithLifecycle(emptyList())

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
    var pocket by remember {
        mutableStateOf(PocketDomain(""))
    }

    if (visibilityPopup) {
        PopupDialog(pocket.name, offsetPopup) { type ->
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
        DialogPocket(pocket) { cur ->
            cur?.let {
                pocket = it
                if (pocket.isValid()) {
                    // notes.add(currency)
                    viewModel.add(pocket)
                }
            }
            visibilityDialog = false
        }
    }
    if (visibilityConfirm) {
        visibilityPopup = false
        DialogConfirm(clazz = pocket) { boo, clazz ->
            val pock = clazz as PocketDomain
            if (boo && pock.isValid()) {
                // notes.remove(currency)
                viewModel.delete(pock)
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
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    text = "Hamyonlar",
                    color = MaterialTheme.customColors.textColor
                )
            }
        }
        items(items = listPocket, key = {
            it.id
        }) { currencyDomain ->
            ItemInPocket(
                pocket = currencyDomain,
                onItemClicked = {
                    visibilityPopup = false
                }, onMenuMoreClicked = { offset ->
                    pocket = currencyDomain
                    offsetPopup = offset
                    visibilityPopup =
                        !visibilityPopup// if (currencyDomain == listCurrency[index]) !visibilityPopup else true

                })
        }

        item(key = "add new pocket") {
            ButtonAdd(text = "Yangi hamyon qo'shish", onClicked = {
                pocket = PocketDomain("")
                visibilityDialog = true
            })
        }
    }
}

