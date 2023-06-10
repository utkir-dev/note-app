package com.example.mynotes.domain.use_cases.data_use_case

import com.example.mynotes.domain.use_cases.currency_use_case.CurrencyUseCases
import javax.inject.Inject

class DataUseCases @Inject constructor(
    var upload: UploadAllData,
    var download: DownloadAllData,
    var currency: CurrencyUseCases,
    var checkAllData: CheckAllData,
    var clearDbLocal: ClearDbLocalUseCase
)