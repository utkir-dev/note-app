package com.example.mynotes.presentation.ui.screens.main.share

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.presentation.ui.directions.BackDirection
import com.example.mynotes.domain.use_cases.data_use_case.GetAllData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ShareViewModelImp @Inject constructor(
    private val direction: BackDirection,
    private val useCases: GetAllData,
    private val ctx: Application
) : ViewModel(), ShareViewModel {
    val success = MutableStateFlow(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.invoke().collect() {
                if (it.keys.isNotEmpty()) {
                    val taskTxt = async {
                        CreateTxt(ctx, it)
                    }
                    val taskHtml = async {
                        CreateHtml(ctx, it)
                    }
                    val taskPdf = async {
                        CreatePDF(ctx, it)
                    }
                    taskTxt.await()
                    taskHtml.await()
                    taskPdf.await()
                    success.value = true
                }
            }
        }
    }


    override fun back() {
        viewModelScope.launch {
            direction.back()
        }
    }
}