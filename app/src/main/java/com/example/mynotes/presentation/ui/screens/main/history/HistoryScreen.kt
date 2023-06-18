package com.example.mynotes.presentation.ui.screens.main.history

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.R
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.buttons.MyButton
import com.example.mynotes.presentation.utils.components.dialogs.DialogAttention
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.items.ItemHistory
import kotlinx.coroutines.launch

class HistoryScreen() : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: HistoryViewModelImp = getViewModel()
        Show(viewModel)
    }
}

@Composable
fun Show(viewModel: HistoryViewModelImp) {
    var visibilityAlert by remember {
        mutableStateOf(false)
    }

    if (visibilityAlert) {
        DialogAttention("Bu ma'lumot serverga saqlanmagan. Internetni yo'qib qaytadan bosing !") {
            visibilityAlert = false
        }
    }

    val lazyColumnListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val shouldStartPaginate = remember {
        derivedStateOf {
            viewModel.canPaginate && (lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -9) >= (lazyColumnListState.layoutInfo.totalItemsCount - 6)
        }
    }
    val historyListPaging = viewModel.historyListForPaging

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value && viewModel.listState == ListState.IDLE)
            viewModel.getHistoryList()
    }

    LazyColumn(
        state = lazyColumnListState,
        modifier = Modifier
            .fillMaxSize()
            .background(brush = MaterialTheme.customColors.backgroundBrush),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
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
                    text = "Tarix",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.customColors.textColor
                )
            }
        }

        items(
            items = historyListPaging.sortedByDescending { it.date },
            key = { it.hashCode() }) { historyItem ->
            ItemHistory(
                item = historyItem,
            ) {
                visibilityAlert = true
                viewModel.checkNotUploadeds()
            }
        }

        item(
            key = viewModel.listState,
        ) {
            when (viewModel.listState) {
                ListState.LOADING -> {
                    Column(
                        modifier = Modifier
                            .fillParentMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        MyText(
                            modifier = Modifier
                                .padding(8.dp),
                            text = "Yuklashni yangilash"
                        )
                        CircularProgressIndicator(color = Color.Black)
                    }
                }
                ListState.PAGINATING -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        MyText(text = "Keyingilari yulanmoqda...")

                        CircularProgressIndicator(color = Color.Black)
                    }
                }
                ListState.PAGINATION_EXHAUST -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        MyButton(text = "Yana yuklash", onClick = { viewModel.downloadNext() }) {}

                        TextButton(
                            modifier = Modifier
                                .padding(top = 8.dp),
                            onClick = {
                                coroutineScope.launch {
                                    lazyColumnListState.animateScrollToItem(0)
                                }
                            },
                            content = {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    MyText(text = "Boshiga qaytish")
                                }
                            }
                        )
                    }
                }
                else -> {}
            }
        }

    }


    // old

//    val historyList by viewModel.history.collectAsStateWithLifecycle(emptyList())
//    var visibilityAlert by remember {
//        mutableStateOf(false)
//    }
//
//    if (visibilityAlert) {
//        DialogAttention("Bu ma'lumot serverga saqlanmagan. Internetni yo'qib qaytadan bosing !") {
//            visibilityAlert = false
//        }
//    }
//    LazyColumn(
//        //  state = lazyColumnListState,
//        modifier = Modifier
//            .fillMaxSize()
//            .background(brush = MaterialTheme.customColors.backgroundBrush),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        item {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 10.dp),
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                IconButton(onClick = {
//                    viewModel.back()
//                }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_arrow_back),
//                        contentDescription = "back arrow",
//                        tint = MaterialTheme.customColors.textColor
//                    )
//                }
//                MyText(
//                    modifier = Modifier
//                        .weight(1.0f)
//                        .padding(start = 8.dp),
//                    text = "Tarix",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.customColors.textColor
//                )
//            }
//        }
//
//        items(items = historyList, key = { it.hashCode() }) { historyItem ->
//            ItemHistory(
//                item = historyItem,
//            ) {
//                visibilityAlert = true
//                viewModel.checkNotUploadeds()
//            }
//        }
//    }
}

