package com.example.mynotes.presentation.ui.screens.main.outcome_currency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.ui.screens.main.outcome_pocket.DialogOutcomeCurrency
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.huminize
import com.example.mynotes.presentation.utils.items.ItemInOutPocket

class OutcomeCurrencyScreen(private val pocketId: String = "") : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: OutcomeCurrencyViewModelImp = getViewModel()
        viewModel.setPocket(pocketId)
        Show(viewModel)
    }
}

@Composable
fun Show(
    viewModel: OutcomeCurrencyViewModelImp
) {
    val pocket by remember { viewModel.pocket }
    val list by viewModel.currencies.collectAsStateWithLifecycle(emptyList())

    val wallets by viewModel.wallets.collectAsStateWithLifecycle(emptyList())

    var visibilityOutcomeDialog by remember {
        mutableStateOf(false)
    }
    var visibilityConfirm by remember {
        mutableStateOf(false)
    }

    if (visibilityOutcomeDialog) {
        DialogOutcomeCurrency(viewModel) {
            visibilityOutcomeDialog = false
        }
    }

    val toolBarHeight = 56.dp
    Scaffold(modifier = Modifier
        .fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.customColors.backgroundBrush)
                    .fillMaxWidth()
                    .height(toolBarHeight)
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
                Icon(
                    painter = painterResource(id = R.drawable.ic_wallet),
                    contentDescription = "back arrow",
                    tint = MaterialTheme.customColors.textColor
                )
                MyText(
                    modifier = Modifier
                        .weight(1.0f)
                        .padding(start = 8.dp),
                    text = "${pocket.name} hamyon",
                    fontSize = 20.sp,
                    color = MaterialTheme.customColors.textColor,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = toolBarHeight)
                    .background(brush = MaterialTheme.customColors.backgroundBrush)
            ) {
                item {
                    MyText(
                        text = if (wallets.isEmpty()) "${pocket.name} hamyonda mablag' mavjud emas" else "Qaysi valyutadan chiqim qilmoqchisiz",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.customColors.subTextColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    )
                }
                itemsIndexed(wallets) { index, wallet ->
                    val cur = list.first { it.id == wallet.currencyId }
                    val name = if (cur != null) cur.name else ""
                    if (wallet.balance >= 0.01) {
                        ItemInOutPocket(
                            text = "${wallet.balance.huminize()}\n$name",

                            onItemClicked = {
                                viewModel.setCurrency(cur)
                                viewModel.setWallet(wallet)
                                visibilityOutcomeDialog = true
                            })
                    }

                }
            }
        }
    )
}