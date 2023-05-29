package com.example.mynotes.presentation.ui.screens.main.pockets

import com.example.mynotes.domain.models.PocketDomain

interface PocketsDirection {
    suspend fun back()
    suspend fun navigateToPocket(pocket: PocketDomain)
}