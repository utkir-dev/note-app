package com.example.mynotes.presentation.ui.screens.main.currencies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModelImp @Inject constructor(
    private val useCases: CurrencyUseCases
) : ViewModel(), CurrencyViewModel {

    val currencies: Flow<List<CurrencyDomain>> = flow {
        emitAll(useCases.getAll.invoke())
    }

    override fun add(currency: CurrencyDomain) {
        viewModelScope.launch(Dispatchers.IO) { useCases.add.invoke(currency) }
    }

    override fun update() {
        // useCases.update.invoke()
    }

    override fun delete(currency: CurrencyDomain) {
        Log.d("!!!", "${currency.name} delete in CurrencyViewModelImp")
        viewModelScope.launch(Dispatchers.IO) { useCases.delete.invoke(currency) }
    }
}