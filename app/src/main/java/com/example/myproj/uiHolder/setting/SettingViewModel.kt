package com.example.myproj.uiHolder.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproj.repository.GuardianRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val repo: GuardianRepository
) : ViewModel() {
    var firstTimeOpenApp = true
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
    val readFromDataStoreColorTheme = repo.colorT.distinctUntilChanged()
    val readFromDataStoreTextSize = repo.textS.distinctUntilChanged()
    val isWantReCreate: MutableStateFlow<Boolean> = MutableStateFlow(false)

}