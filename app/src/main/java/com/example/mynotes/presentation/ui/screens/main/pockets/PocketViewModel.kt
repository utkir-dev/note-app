package com.example.mynotes.presentation.ui.screens.main.pockets

import com.example.mynotes.domain.models.PocketDomain
import kotlinx.coroutines.flow.Flow


interface PocketViewModel {
    val pockets: Flow<List<PocketDomain>>
    fun add(pocket: PocketDomain)
    fun update(pocket: PocketDomain)
    fun delete(pocket: PocketDomain)
    fun back()
}