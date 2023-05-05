package com.example.mynotes.presentation.ui.screens.main.balance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.text.MyText
import com.example.mynotes.presentation.utils.theme.background1

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
            .background(brush = background1())
    ) {
        item { MyText(text = "Balance: 5000$") }
        item { MyText(text = "Dollar:  200$") }
        item { MyText(text = "TL:  2000 Tl") }
    }
}