package com.example.myproj.uiHolder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.myproj.model.ApiResult
import com.example.myproj.repository.GuardianRepository
import com.example.myproj.uiState.GuardianUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GuardianViewModel (private val repo: GuardianRepository,
                         private val savedStateHandle: SavedStateHandle) : ViewModel() {
//    init {
//        viewModelScope.launch {
//            repo.collectPageSize()
//        }
//    }
    val guardianData: Flow<PagingData<ApiResult>> = repo.getGuardianData().cachedIn(viewModelScope)
    val guardianDataBySection: Flow<PagingData<ApiResult>> = repo.getGuardianDataBySection("world").cachedIn(viewModelScope)
    val guardianDataBySectionScience: Flow<PagingData<ApiResult>> = repo.getGuardianDataBySection("science").cachedIn(viewModelScope)
    val guardianDataBySectionSport: Flow<PagingData<ApiResult>> = repo.getGuardianDataBySection("sport").cachedIn(viewModelScope)
    val guardianDataBySectionEnvironment: Flow<PagingData<ApiResult>> = repo.getGuardianDataBySection("environment").cachedIn(viewModelScope)

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
