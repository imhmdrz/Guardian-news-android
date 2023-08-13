package com.example.myproj.uiHolder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myproj.model.ApiResult
import com.example.myproj.repository.GuardianRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GuardianViewModel (private val repo: GuardianRepository,
                         private val savedStateHandle: SavedStateHandle) : ViewModel() {
    fun guardianDataHome(): Flow<PagingData<ApiResult>> = repo.numberOI
        .map { repo.getGuardianData(it.toInt()) }
        .flattenConcat().cachedIn(viewModelScope)
    fun guardianDataBySectionWorld(): Flow<PagingData<ApiResult>> = repo.numberOI
        .map { repo.getGuardianDataBySection("world",it.toInt()) }
        .flattenConcat().cachedIn(viewModelScope)
    fun guardianDataBySectionScience(): Flow<PagingData<ApiResult>> = repo.numberOI
        .map { repo.getGuardianDataBySection("science",it.toInt()) }
        .flattenConcat().cachedIn(viewModelScope)
    fun guardianDataBySectionSport(): Flow<PagingData<ApiResult>> = repo.numberOI
        .map { repo.getGuardianDataBySection("sport",it.toInt()) }
        .flattenConcat().cachedIn(viewModelScope)
    fun guardianDataBySectionEnvironment(): Flow<PagingData<ApiResult>> = repo.numberOI
        .map { repo.getGuardianDataBySection("environment",it.toInt()) }
        .flattenConcat().cachedIn(viewModelScope)


    fun saveToDataStoreNOI(numberOfItems: String) = viewModelScope.launch{
        repo.saveToDataStoreNOI(numberOfItems)
    }
    fun saveToDataStoreOrderBy(orderBy: String) = viewModelScope.launch{
        repo.saveToDataStoreOB(orderBy)
    }
    fun saveToDataStoreFromDate(fromDate: String) = viewModelScope.launch{
        repo.saveToDataStoreFD(fromDate)
    }
    fun saveToDataStoreColorTheme(colorTheme: String) = viewModelScope.launch{
        repo.saveToDataStoreCT(colorTheme)
    }
    fun saveToDataStoreTextSize(textSize: String) = viewModelScope.launch{
        repo.saveToDataStoreTS(textSize)
    }

    val readFromDataStoreNOI = repo.numberOI.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = "10"
    )

    val readFromDataStoreOrderBy = repo.orderB.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = "newest"
    )

    val readFromDataStoreFromDate = repo.fromD.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = "2021-01-01"
    )

    val readFromDataStoreColorTheme = repo.colorT.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = "white"
    )

    val readFromDataStoreTextSize = repo.textS.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = "medium"
    )


}
