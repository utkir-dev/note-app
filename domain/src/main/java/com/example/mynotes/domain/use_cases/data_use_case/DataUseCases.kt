package com.example.mynotes.domain.use_cases.data_use_case

import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import javax.inject.Inject

class DataUseCases @Inject constructor(
    val upload: UploadAllData,
    val download: DownloadAllData,
    val currency: CurrencyUseCases,
    val checkAllData: CheckAllData,
    val clearDbLocal: ClearDbLocalUseCase,
    val notUploadedDataCount: GetNotUploadedDataCount
)