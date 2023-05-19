package com.example.mynotes.presentation

import android.os.Bundle
import android.transition.Slide
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.example.mynotes.presentation.ui.dispatcher.NavigationHandler
import com.example.mynotes.presentation.ui.screens.splash.SplashScreen
import com.example.mynotes.presentation.utils.components.image.MyNotesTheme
import com.example.mynotes.presentation.utils.theme.ThemeState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationHandler: NavigationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeState.darkModeState.value = when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> true
            else -> false
        }
        setContent {
            MyNotesTheme(darkTheme = ThemeState.darkModeState.value) {
                Navigator(SplashScreen()) { navigator ->
                    LaunchedEffect(key1 = navigator) {
                        navigationHandler.navigationBuffer
                            .onEach { it.invoke(navigator) }
                            .collect()
                    }

                    CurrentScreen()
                }
            }
        }
    }
}