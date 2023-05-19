package com.example.mynotes.presentation.ui.screens.main.outcome_pocket

import com.example.mynotes.domain.models.PocketDomain
import kotlinx.coroutines.flow.Flow


interface OutcomePocketViewModel {
    val pockets: Flow<List<PocketDomain>>
    fun back()
    fun navigateToOutcomeCurrency(pocketDomain: PocketDomain)

}