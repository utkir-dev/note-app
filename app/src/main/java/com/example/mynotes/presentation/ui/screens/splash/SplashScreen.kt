package com.example.mynotes.presentation.ui.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.ui.dispatcher.AppScreen
import com.example.mynotes.presentation.utils.components.text.MyText
import javax.inject.Inject

class SplashScreen @Inject constructor() : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: SplashViewModelIml = getViewModel()
        ShowSplash()
    }
}

@Composable
private fun ShowSplash() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        MyText("Splash")
    }
}