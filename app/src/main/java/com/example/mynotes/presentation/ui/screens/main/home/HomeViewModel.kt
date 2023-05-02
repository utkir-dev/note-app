package com.example.mynotes.presentation.ui.screens.main.home

import com.example.mynotes.presentation.ui.directions.common.DirectionType

interface HomeViewModel {
    fun onEventDispatcher(type: DirectionType)
}