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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch



class GuardianViewModel(
    private val repo: GuardianRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val guardianDataHome : Flow<PagingData<ApiResult>> = repo.numberOI.map {str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianData(str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)

    val guardianDataBySectionWorld: Flow<PagingData<ApiResult>> = repo.numberOI.map {str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianDataBySection("world", str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)

    val guardianDataBySectionScience: Flow<PagingData<ApiResult>> = repo.numberOI.map {str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianDataBySection("science", str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)

    val guardianDataBySectionSport: Flow<PagingData<ApiResult>> = repo.numberOI.map {str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianDataBySection("sport", str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)

    val guardianDataBySectionEnvironment: Flow<PagingData<ApiResult>> = repo.numberOI.map {str ->
            repo.orderB.map {order->
                repo.fromD.map {from->
                    repo.getGuardianDataBySection("environment", str.toInt(),order,from)
                }.flattenConcat()
            }.flattenConcat()
        }.flattenConcat().cachedIn(viewModelScope)
}
