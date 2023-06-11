package com.example.mynotes.presentation.ui.screens.main.share

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.presentation.ui.directions.BackDirection
import com.example.mynotes.domain.use_cases.data_use_case.GetAllData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class ShareViewModelImp @Inject constructor(
    private val direction: BackDirection,
    private val useCases: GetAllData,
    private val ctx: Application
) : ViewModel(), ShareViewModel {
    val success = MutableStateFlow(false)
    val txtBytes = MutableStateFlow(byteArrayOf(0))
    val htmlBytes = MutableStateFlow(byteArrayOf(0))
    val pdfBytes = MutableStateFlow(byteArrayOf(0))

    init {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.invoke().collect() {
                if (it.keys.isNotEmpty()) {
                    val taskTxt = CreateTxt(ctx, it)
                    val taskHtml = CreateHtml(ctx, it)
                    val taskPdf = CreatePDF(ctx, it)

                    combine(taskHtml, taskTxt, taskPdf) { h, t, p ->
                        htmlBytes.value = h
                        txtBytes.value = t
                        pdfBytes.value = p
                        success.value = true
                    }.collect()
                } else {
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