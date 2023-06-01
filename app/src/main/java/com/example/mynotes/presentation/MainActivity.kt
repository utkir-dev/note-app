package com.example.mynotes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.*
import androidx.work.*
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.example.mynotes.domain.background_task.CheckDatabase
import com.example.mynotes.presentation.ui.dispatcher.NavigationHandler
import com.example.mynotes.presentation.ui.screens.splash.SplashScreen
import com.example.mynotes.presentation.utils.components.image.MyNotesTheme
import com.example.mynotes.presentation.utils.contstants.KEY_WORK_MANAGER_ID
import com.example.mynotes.presentation.utils.theme.ThemeState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationHandler: NavigationHandler
    private val workManager by lazy {
        WorkManager.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createPeriodicWorkRequest()
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

    private fun createPeriodicWorkRequest() {
        val builder = Data.Builder()
        builder.putString(KEY_WORK_MANAGER_ID, "11111")
        val data = builder.build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val checker = PeriodicWorkRequestBuilder<CheckDatabase>(
            15, TimeUnit.MINUTES
        ).setInputData(data)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "periodicDatabaseChecker",
            ExistingPeriodicWorkPolicy.KEEP,
            checker
        )
    }
}