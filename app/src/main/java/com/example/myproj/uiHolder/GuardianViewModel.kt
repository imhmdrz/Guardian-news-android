package com.example.myproj.uiHolder




import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myproj.model.ApiResult
import com.example.myproj.repository.GuardianRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch



@OptIn(FlowPreview::class)
class GuardianViewModel(
    private val repo: GuardianRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var guardianDataHome : Flow<PagingData<ApiResult>> = repo.numberOI.map {str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianData(str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)

    var guardianDataBySectionWorld: Flow<PagingData<ApiResult>> = repo.numberOI.map {str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianDataBySection("world", str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)

    var guardianDataBySectionScience: Flow<PagingData<ApiResult>> = repo.numberOI.map { str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianDataBySection("science", str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)

    var guardianDataBySectionSport: Flow<PagingData<ApiResult>> = repo.numberOI.map { str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianDataBySection("sport", str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)

    var guardianDataBySectionEnvironment: Flow<PagingData<ApiResult>> = repo.numberOI.map { str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianDataBySection("environment", str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)

    fun reCreateDataStore() = viewModelScope.launch {
        guardianDataHome = repo.numberOI.map {str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianData(str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)
        guardianDataBySectionWorld = repo.numberOI.map {str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianDataBySection("world", str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)
        guardianDataBySectionScience = repo.numberOI.map {str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianDataBySection("science", str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)
        guardianDataBySectionSport = repo.numberOI.map {str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianDataBySection("sport", str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)
        guardianDataBySectionEnvironment = repo.numberOI.map {str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianDataBySection("environment", str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)
    }
}
