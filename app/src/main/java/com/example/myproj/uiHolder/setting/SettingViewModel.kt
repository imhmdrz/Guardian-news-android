package com.example.myproj.uiHolder.setting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproj.repository.GuardianRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val repo: GuardianRepository
) : ViewModel() {
    var firstTimeOpenApp = true
    var textSize = "Small"
    fun saveToDataStore(
        numberOfItem: String? = null,
        orderBy: String? = null,
        fromDate: String? = null,
        colorTheme: String? = null,
        textSize: String? = null
    ) = viewModelScope.launch {
        repo.saveToDataStore(numberOfItem, orderBy, fromDate, colorTheme, textSize)
    }
    val readFromDataStoreNOI = repo.numberOI.distinctUntilChanged().shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )

    val readFromDataStoreOrderBy = repo.orderB.distinctUntilChanged().shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )

    val readFromDataStoreFromDate = repo.fromD.distinctUntilChanged().shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )

    val readFromDataStoreColorTheme = repo.colorT.distinctUntilChanged().shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed()
    )

    val readFromDataStoreTextSize = repo.textS.distinctUntilChanged().shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )
}