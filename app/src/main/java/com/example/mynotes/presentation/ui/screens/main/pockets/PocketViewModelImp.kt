package com.example.mynotes.presentation.ui.screens.main.pockets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.domain.use_cases.pocket_use_case.PocketUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PocketViewModelImp @Inject constructor(
    private val useCases: PocketUseCases,
    private val direction: PocketDirection
) : ViewModel(), PocketViewModel {


    override val pockets: Flow<List<PocketDomain>> = flow {
        emitAll(useCases.getAll.invoke())
    }

    override fun add(pocket: PocketDomain) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.add.invoke(pocket)
        }
    }

    override fun update(pocket: PocketDomain) {

    }

    override fun delete(pocket: PocketDomain) {
        viewModelScope.launch(Dispatchers.IO) { useCases.delete.invoke(pocket) }

    }

    override fun back() {
        viewModelScope.launch(Dispatchers.Default) {
            direction.back()
        }
    }
}