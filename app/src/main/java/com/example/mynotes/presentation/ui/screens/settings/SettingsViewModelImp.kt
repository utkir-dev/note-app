package com.example.mynotes.presentation.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.ResponseResult
import com.example.mynotes.domain.use_cases.auth_use_case.SignUpUseCase
import com.example.mynotes.domain.use_cases.data_use_case.DataUseCases
import com.example.mynotes.domain.use_cases.shared_pref_use_case.SharedPrefUseCases
import com.example.mynotes.presentation.ui.directions.BackDirection
import com.example.mynotes.presentation.ui.directions.common.UiState
import com.example.mynotes.contstants.KEY_PINCODE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SettingsViewModelImp @Inject constructor(
    private val direction: BackDirection,
    private val useCases: SharedPrefUseCases
) : ViewModel(), SettingsViewModel {
    val success = MutableStateFlow(false)
    val pincode = MutableStateFlow("")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            pincode.value = useCases.getString.invoke(KEY_PINCODE)
        }
    }

    override fun savePickode(pincode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.saveString.invoke(KEY_PINCODE, pincode)
        }
    }


    override fun back() {
        viewModelScope.launch {
            direction.back()
        }
    }
}