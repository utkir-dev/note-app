package com.example.mynotes.presentation.ui.screens.main.balance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.huminize
import com.example.mynotes.presentation.utils.items.ItemInOutPocket

class BalanceScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: BalanceViewModelImp = getViewModel()
        ShowBalance(viewModel)
    }
}

@Composable
fun ShowBalance(viewModel: BalanceViewModelImp) {
    val balanceList by viewModel.balances.collectAsStateWithLifecycle(emptyList())
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
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
                    text = "BalanceDomain",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.customColors.textColor
                )
            }
        }
        item {
            MyText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = "Jami: ${balanceList.sumOf { it.amount * (1 / it.rate) }.huminize()} $",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.customColors.textColor
            )
        }
        items(items = balanceList, key = {
            it.hashCode() + 1000
        }) { balanceDomain ->
            ItemInOutPocket(
                text = "${balanceDomain.currencyName}: ${balanceDomain.amount.huminize()}",
                onItemClicked = {})
        }
    }
}
