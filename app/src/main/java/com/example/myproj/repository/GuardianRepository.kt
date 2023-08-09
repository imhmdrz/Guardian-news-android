package com.example.myproj.repository


import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myproj.loadDataFromInternet.GuardianApiService
import com.example.myproj.model.ApiResult
import com.example.myproj.paggingSource.PagingMediator
import com.example.myproj.roomDataBase.NewsDataBase
import kotlinx.coroutines.flow.Flow

//object GuardianRepository {
//    private val retrofitService: GuardianApiService = RetrofitIns
//        .getRetrofitInstance().create(GuardianApiService::class.java)
//    suspend fun getGuardianData(str: String?): Flow<PagingData<ApiResult>> = withContext(Dispatchers.IO) {
//            Pager(
//                config = PagingConfig(
//                    pageSize = 10,
//                    enablePlaceholders = false
//                ),
//                pagingSourceFactory = { PagingSource(retrofitService, str) }
//            ).flow
//        }
//    }
class GuardianRepository(private val apiService: GuardianApiService,
                         private val db : NewsDataBase){
    @OptIn(ExperimentalPagingApi::class)
    fun getGuardianDataBySection(section: String): Flow<PagingData<ApiResult>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PagingMediator(
                section,
                apiService,
                db
            ),
            pagingSourceFactory = { db.newsDao().getData(section) }
        ).flow
    }
    @OptIn(ExperimentalPagingApi::class)
    fun getGuardianData(): Flow<PagingData<ApiResult>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PagingMediator(
                null,
                apiService,
                db
            ),
            pagingSourceFactory = { db.newsDao().getAllData() }
        ).flow
    }
    companion object {
        private const val PAGE_SIZE = 10
    }
}