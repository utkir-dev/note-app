package com.example.mynotes.presentation.ui.dispatcher

interface AppNavigator {
    suspend fun navigateTo(screen: AppScreen)
    suspend fun navigateTo(screens: List<AppScreen>)
    suspend fun replace(screen: AppScreen)
    suspend fun replaceAll(screen: AppScreen)
    suspend fun back()
    suspend fun <T : AppScreen> backUntil(clazz: Class<T>)
    suspend fun replaceTo(screen: AppScreen)
}