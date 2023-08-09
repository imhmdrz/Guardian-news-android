package com.example.myproj.uiHolder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.myproj.repository.GuardianRepository
import com.example.myproj.uiState.GuardianUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class GuardianViewModel constructor(private val repo: GuardianRepository) : ViewModel() {

    private val _uiState: List<MutableStateFlow<GuardianUiState>> =
        List(5) { MutableStateFlow(GuardianUiState.Loading) }
    val uiState: List<MutableStateFlow<GuardianUiState>> get() = _uiState

    init {
        getGuardianData()
    }

    fun refreshData() = getGuardianData()
    private fun getGuardianData() { //get data from repository
        viewModelScope.launch {
            repo.getGuardianData().cachedIn(viewModelScope)
                .let {
                    _uiState[0].value = GuardianUiState.Success(it)
                }
        }
        viewModelScope.launch {
            repo.getGuardianDataBySection("world").cachedIn(viewModelScope)
                .let {
                    _uiState[1].value = GuardianUiState.Success(it)
                }
        }
        viewModelScope.launch {
            repo.getGuardianDataBySection("science").cachedIn(viewModelScope)
                .let {
                    _uiState[2].value = GuardianUiState.Success(it)
                }
        }
        viewModelScope.launch {
            repo.getGuardianDataBySection("sport").cachedIn(viewModelScope)
                .let {
                    _uiState[3].value = GuardianUiState.Success(it)
                }
        }
        viewModelScope.launch {
            repo.getGuardianDataBySection("environment").cachedIn(viewModelScope)
                .let {
                    _uiState[4].value = GuardianUiState.Success(it)
                }
        }
    }
}