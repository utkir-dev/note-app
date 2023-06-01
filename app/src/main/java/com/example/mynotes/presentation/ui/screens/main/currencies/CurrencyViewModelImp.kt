package com.example.mynotes.presentation.ui.screens.main.currencies

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.models.CurrencyDomain
import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModelImp @Inject constructor(
    private val useCases: CurrencyUseCases,
    private val direction: CurrencyDirection
) : ViewModel(), CurrencyViewModel {
    init {
        viewModelScope.launch {
            if (useCases.getCount.invoke() == 0) {
                useCases.add.invoke(
                    CurrencyDomain(
                        id = UUID.randomUUID().toString(),
                        name = "dollar",
                        rate = 1.0,
                        date = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    override var currencies: Flow<List<CurrencyDomain>> = flow {
        emitAll(useCases.getAll.invoke())
    }

    override fun add(currency: CurrencyDomain) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.add.invoke(currency)
        }
    }

    override fun update(currency: CurrencyDomain) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.update.invoke(currency)
        }
    }

    override fun delete(currency: CurrencyDomain) {
        viewModelScope.launch(Dispatchers.IO) { useCases.delete.invoke(currency) }
    }

    override fun back() {
        viewModelScope.launch(Dispatchers.Default) {
            direction.back()
        }
    }

    private fun checkValidation() {
        useCases
    }
}