package com.example.mynotes.presentation.ui.screens.main.balance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText

class BalanceScreen : AppScreen() {
    @Composable
    override fun Content() {
        ShowBalance()
    }
}

@Composable
fun ShowBalance() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = MaterialTheme.customColors.backgroundBrush)
    ) {
        item { MyText(text = "Balance: 5000$") }
        item { MyText(text = "Dollar:  200$") }
        item { MyText(text = "TL:  2000 Tl") }
    }
}