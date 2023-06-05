package com.example.mynotes.presentation.ui.screens.main.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.mynotes.presentation.utils.components.dialogs.DialogAttention
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.items.ItemHistory

class HistoryScreen() : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: HistoryViewModelImp = getViewModel()
        Show(viewModel)
    }
}

@Composable
fun Show(viewModel: HistoryViewModelImp) {
    val historyList by viewModel.history.collectAsStateWithLifecycle(emptyList())

    var visibilityAlert by remember {
        mutableStateOf(false)
    }

    if (visibilityAlert) {
        DialogAttention("Bu ma'lumot serverga saqlanmagan. Internetni yo'qib qaytadan bosing !") {
            visibilityAlert = false
        }
    }
    LazyColumn(
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

        items(items = historyList, key = { it.hashCode() }) { historyItem ->
            ItemHistory(
                item = historyItem,
            ) {
                visibilityAlert = true
                viewModel.checkNotUploadeds()
            }
        }
    }
}


