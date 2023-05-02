package com.example.mynotes.presentation.ui.dispatcher

import kotlinx.coroutines.flow.MutableSharedFlow

interface NavigationHandler {
    val navigationBuffer: MutableSharedFlow<NavigationArgs>
}