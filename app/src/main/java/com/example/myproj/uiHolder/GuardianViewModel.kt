package com.example.myproj.uiHolder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myproj.model.ApiResult
import com.example.myproj.repository.GuardianRepository
import com.example.myproj.uiState.GuardianUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onStart

class GuardianViewModel (private val repo: GuardianRepository,
                         private val savedStateHandle: SavedStateHandle,
                         private val section:String?) : ViewModel() {

    private val _uiState: List<MutableStateFlow<GuardianUiState>> =
        List(5) { MutableStateFlow(GuardianUiState.Loading) }
    val uiState: List<MutableStateFlow<GuardianUiState>> get() = _uiState
    init {
        getGuardianData()
    }
    private fun getGuardianData() {
        repo.getGuardianData().cachedIn(viewModelScope)
            .let {
                _uiState[0].value = GuardianUiState.Success(it)
            }
        repo.getGuardianDataBySection("world").cachedIn(viewModelScope)
            .let {
                _uiState[1].value = GuardianUiState.Success(it)
            }
        repo.getGuardianDataBySection("science").cachedIn(viewModelScope)
            .let {
                _uiState[2].value = GuardianUiState.Success(it)
            }
        repo.getGuardianDataBySection("sport").cachedIn(viewModelScope)
            .let {
                _uiState[3].value = GuardianUiState.Success(it)
            }
        repo.getGuardianDataBySection("environment").cachedIn(viewModelScope)
            .let {
                _uiState[4].value = GuardianUiState.Success(it)
            }
    }
}