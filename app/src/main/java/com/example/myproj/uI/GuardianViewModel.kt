package com.example.myproj.uI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproj.loadDataFromInternet.ApiResult
import com.example.myproj.loadDataFromInternet.ThumbnailFields
import com.example.myproj.repository.GuardianRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GuardianViewModel(private val repo: GuardianRepository = GuardianRepository) : ViewModel() {
    private var _dataHome: MutableStateFlow<List<ApiResult>> = MutableStateFlow(emptyList())
    val dataHome get() = _dataHome
    private var _dataWorld: MutableStateFlow<List<ApiResult>> = MutableStateFlow(emptyList())
    val dataWorld get() = _dataWorld
    private var _dataScience: MutableStateFlow<List<ApiResult>> = MutableStateFlow(emptyList())
    val dataScience get() = _dataScience
    private var _dataSport: MutableStateFlow<List<ApiResult>> = MutableStateFlow(emptyList())
    val dataSport get() = _dataSport
    private var _dataEnvironment: MutableStateFlow<List<ApiResult>> = MutableStateFlow(emptyList())
    val dataEnvironment get() = _dataEnvironment

    init {
        getGuardianData()
    }

    private fun getGuardianData() {
        viewModelScope.launch {
            repo.getGuardianData(null)!!.collect() {
                if (it.response == null) _dataHome.value = apiResultsERROR()
                else _dataHome.value = it.response.results
            }
        }
        viewModelScope.launch {
            repo.getGuardianData("world")!!.collect() {
                if (it.response == null) _dataWorld.value = apiResultsERROR()
                else _dataWorld.value = it.response.results
            }
        }
        viewModelScope.launch {
            repo.getGuardianData("science")!!.collect() {
                if (it.response == null) _dataScience.value = apiResultsERROR()
                else _dataScience.value = it.response.results
            }
        }
        viewModelScope.launch {
            repo.getGuardianData("sport")!!.collect() {
                if (it.response == null) _dataSport.value = apiResultsERROR()
                else _dataSport.value = it.response.results
            }
        }
        viewModelScope.launch {
            repo.getGuardianData("environment")!!.collect() {
                if (it.response == null) _dataEnvironment.value = apiResultsERROR()
                else _dataEnvironment.value = it.response.results
            }
        }
    }
    private fun apiResultsERROR() = List(1) {
        ApiResult("NO INTERNET CONNECTION", "", "", "", "", "", "", "", ThumbnailFields("",""), false, "", "")
    }
}