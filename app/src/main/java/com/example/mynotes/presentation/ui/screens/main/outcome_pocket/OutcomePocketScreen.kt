package com.example.mynotes.presentation.ui.screens.main.outcome_pocket

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
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.items.ItemInOutPocket

class OutcomePocketScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: OutcomePocketViewModelImp = getViewModel()
        ShowIncomes(viewModel)
    }
}

@Composable
fun ShowIncomes(
    viewModel: OutcomePocketViewModelImp
) {
    val walletsByOwners by viewModel.walletsByOwners.collectAsStateWithLifecycle(emptyList())
    val pockets by viewModel.pockets.collectAsStateWithLifecycle(emptyList())
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
                MyText(
                    modifier = Modifier
                        .weight(1.0f)
                        .padding(start = 8.dp),
                    text = "Chiqim",
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
                        text = if (pockets.isEmpty()) "Hamyon mavjud emas" else "Qaysi hamyondan chiqim qilmoqchisiz",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.customColors.subTextColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    )
                }
                itemsIndexed(pockets) { index, pocket ->
                    val chips = walletsByOwners.filter { it.ownerId == pocket.id }
                    ItemInOutPocket(
                        text = pocket.name,
                        chipsVisibility = true,
                        chips = chips,
                        onItemClicked = {
                            if (chips.isNotEmpty()) {
                                viewModel.navigateToOutcomeCurrency(pocket)
                            }
                        })
                }
            }
        }
    )
}