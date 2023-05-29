package com.example.mynotes.presentation.ui.screens.main.pocket_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.extensions.huminize
import com.example.mynotes.presentation.utils.items.ItemHistoryPerson
import com.example.mynotes.presentation.utils.items.ItemHistoryPocket

class PocketScreen(val pocket: PocketDomain) : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: PocketViewModelImp = getViewModel()
        Show(viewModel, pocket)
    }
}

@Composable
fun Show(
    viewModel: PocketViewModelImp,
    pocket: PocketDomain
) {
    viewModel.setPocket(pocket)
    val wallets by viewModel.walletsByOwners.collectAsStateWithLifecycle(emptyList())
    val history by viewModel.history.collectAsStateWithLifecycle(emptyList())

    val toolBarHeight = 56.dp
    Scaffold(modifier = Modifier
        .fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(toolBarHeight)
            ) {
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.customColors.backgroundBrush)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
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
                        text = pocket.name + " hamyon",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.customColors.textColor,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        },

        content = {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .background(MaterialTheme.customColors.backgroundBrush)
                    .padding(top = toolBarHeight)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                LazyColumn(
                    Modifier
                        .background(MaterialTheme.customColors.backgroundBrush)
                        .fillMaxSize()
                        .padding(8.dp),
                    // verticalArrangement = Arrangement.Top
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MyText(
                                text = "Balans:",
                                color = MaterialTheme.customColors.textColor
                            )
                            Column {
                                //   val list = wallets.filter { it.currencyBalance >= 0.01 }
                                if (wallets.isEmpty()) {
                                    MyText(
                                        text = "hamyon bo'sh",
                                        color = MaterialTheme.customColors.textColor,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                } else {
                                    wallets.forEach {
                                        MyText(
                                            text = "${it.currencyBalance.huminize()} ${it.currencyName}",
                                            color = MaterialTheme.customColors.textColor,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (history.isNotEmpty()) {
                        item {
                            MyText(
                                text = "Tarixi:",
                                color = MaterialTheme.customColors.textColor
                            )
                        }
                        items(items = history, key = { it.hashCode() }) { historyItem ->
                            ItemHistoryPocket(
                                item = historyItem,
                                pocketName = pocket.name,
                                onItemClicked = { })
                        }
                    }
                }
            }
        }
    )
}


