package com.example.mynotes.di

import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.dispatcher.NavigationDispatcher
import com.example.mynotes.presentation.ui.dispatcher.NavigationHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {
    @Binds
    @Singleton
    fun bindNavigationHandler(dispatcher: NavigationDispatcher): NavigationHandler

    @Binds
    @Singleton
    fun bindAppNavigation(dispatcher: NavigationDispatcher): AppNavigator
}