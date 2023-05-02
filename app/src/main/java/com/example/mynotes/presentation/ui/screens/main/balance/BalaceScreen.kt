package com.example.mynotes.presentation.ui.screens.main.balance

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mynotes.presentation.ui.dispatcher.AppScreen

class BalanceScreen : AppScreen() {
    @Composable
    override fun Content() {
        ShowBalance()
    }
}

@Composable
fun ShowBalance() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { Text(text = "Balance: 5000$") }
        item { Text(text = "Dollar:  200$") }
        item { Text(text = "TL:  2000 Tl") }
    }
}