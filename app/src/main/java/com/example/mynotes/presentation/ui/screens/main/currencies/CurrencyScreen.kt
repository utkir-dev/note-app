package com.example.mynotes.presentation.ui.screens.main.currencies

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.domain.models.CurrencyRemote
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.DialogCurrency
import com.example.mynotes.presentation.utils.components.buttons.ButtonAdd
import com.example.mynotes.presentation.utils.components.dialogs.PopupDialog
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.items.ItemCommon

class CurrencyScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: CurrencyViewModelImp = getViewModel()
        val list = viewModel.currencies.collectAsState(initial = emptyList())
        ShowCurrencies(viewModel, list)
    }
}

@Composable
fun ShowCurrencies(
    viewModel: CurrencyViewModelImp,
    list: State<List<CurrencyRemote>>
) {
    var visibilityPopup by remember {
        mutableStateOf(false)
    }
    var offsetPopup by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    var visibilityDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var textPopup by remember {
        mutableStateOf("")
    }
    if (visibilityPopup) {
        PopupDialog(textPopup, offsetPopup)
    }
    if (visibilityDialog) {
        DialogCurrency { currencyName, currencyRate, isValid ->
            if (isValid) {
                viewModel.add(currencyName, currencyRate)
            }
            visibilityDialog = false
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput("screenToutch") {
                detectTapGestures(
                    onPress = {
                        visibilityPopup = false
                    }
                )
            }
            .scrollable(
                orientation = Orientation.Vertical,
                state = rememberScrollableState { delta ->
                    visibilityPopup = false
                    //offset += delta
                    delta
                }
            )
            .background(brush = MaterialTheme.customColors.backgroundBrush)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {

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

        item { ItemCommon(text = "Dollar US", onItemClicked = {}, onMenuMoreClicked = {}) }

        itemsIndexed(list.value) { index, value ->
            ItemCommon(text = value.name, onItemClicked = {}, onMenuMoreClicked = {
                offsetPopup = it //Offset(x = it.x, y = it.y * (index + 1))
                textPopup = value.name
                visibilityPopup = !visibilityPopup

            })
        }
        item {
            ButtonAdd(text = "Yangi valyuta qo'shish", onClicked = {
                visibilityDialog = true
            })
        }
    }
}