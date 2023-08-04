package com.example.myproj.uiHolder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproj.model.ApiResult
import com.example.myproj.repository.GuardianRepository
import com.example.myproj.uiState.GuardianUiState
import com.example.myproj.utils.apiResultsERROR
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GuardianViewModel(private val repo: GuardianRepository = GuardianRepository) : ViewModel() {

    private val _uiState : List<MutableStateFlow<GuardianUiState>> = List(5){MutableStateFlow(GuardianUiState.Loading)}
    val uiState : List<MutableStateFlow<GuardianUiState>> get() = _uiState
    init {
        getGuardianData()
    }
    fun refreshData() = getGuardianData()
    private fun getGuardianData() { //get data from repository
        viewModelScope.launch {
            repo.getGuardianData(null)!!.collect() {
                if (it.response == null) _uiState[0].value = GuardianUiState.Error("NO INTERNET CONNECTION")
                else _uiState[0].value = GuardianUiState.Success(it.response.results)
            }
        }
        viewModelScope.launch {
            repo.getGuardianData("world")!!.collect() {
                if (it.response == null) _uiState[1].value = GuardianUiState.Error("NO INTERNET CONNECTION")
                else _uiState[1].value = GuardianUiState.Success(it.response.results)
            }
        }
        viewModelScope.launch {
            repo.getGuardianData("science")!!.collect() {
                if (it.response == null) _uiState[2].value = GuardianUiState.Error("NO INTERNET CONNECTION")
                else _uiState[2].value = GuardianUiState.Success(it.response.results)
            }
        }
        viewModelScope.launch {
            repo.getGuardianData("sport")!!.collect() {
                if (it.response == null) _uiState[3].value = GuardianUiState.Error("NO INTERNET CONNECTION")
                else _uiState[3].value = GuardianUiState.Success(it.response.results)
            }
        }
        viewModelScope.launch {
            repo.getGuardianData("football")!!.collect() {
                if (it.response == null) _uiState[4].value = GuardianUiState.Error("NO INTERNET CONNECTION")
                else _uiState[4].value = GuardianUiState.Success(it.response.results)
            }
        }
    }
}