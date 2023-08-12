package com.example.myproj.paggingSource

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.android.codelabs.paging.db.RemoteKeys
import com.example.myproj.loadDataFromInternet.GuardianApiService
import com.example.myproj.model.ApiResult
import com.example.myproj.model.GuardianApiResponse
import com.example.myproj.roomDataBase.NewsDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PagingMediator(
    private val section: String?,
    private val apiService: GuardianApiService,
    private val db: NewsDataBase
) : RemoteMediator<Int, ApiResult>() {
    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ApiResult>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        Log.d("PagingMediator", "load: $page $loadType")
        delay(2000)
        try {
            val response = apiService.getGuardianData(section, page, state.config.pageSize)
            val endOfPageReached = response.body()?.response?.results?.isEmpty() == true
            Log.d("PagingMediator", "load: ${response.body()?.response?.results} ")
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys(section)
                    if(section == null){
                        val listFiltered = response.body()?.response?.results?.filter {
                            it.sectionId != "science" && it.sectionName != "world" && it.sectionName != "sport" && it.sectionName != "environment"
                        }
                        db.newsDao().deleteAllData(listFiltered ?: emptyList())
                    }else{
                        db.newsDao().deleteData(section)
                    }

                    Log.d("PagingMediator", "load 2:$section ${response.body()?.response?.results} ")
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPageReached) null else page + 1
                val keys = response.body()?.response?.results?.map {
                    RemoteKeys(
                        NewsId = it.id,
                        section = section,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                withContext(Dispatchers.IO){
                    if(section == null){
                        delay(1500)
                        val keysFiltered = keys?.filter {
                            it.section != "world" && it.section != "science" && it.section != "sport" && it.section != "environment"
                        }
                        val listFiltered = response.body()?.response?.results?.filter {
                            it.sectionId != "science" && it.sectionName != "world" && it.sectionName != "sport" && it.sectionName != "environment"
                        }
                        db.remoteKeysDao().insertAll(keysFiltered)
                        Log.d("PagingMediator", "load 3:$section ${response.body()?.response?.results} ")
                        db.newsDao().insertAll(listFiltered ?: listOf())
                        Log.d("PagingMediator", "load 4: ${response.body()?.response?.results} ")
                    }else{
                        db.remoteKeysDao().insertAll(keys)
                        Log.d("PagingMediator", "load 3:$section ${response.body()?.response?.results} ")
                        db.newsDao().insertAll(response.body()?.response?.results ?: listOf())
                        Log.d("PagingMediator", "load 4: ${response.body()?.response?.results} ")
                    }

                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPageReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ApiResult>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { ApiResult ->
                Log.d("PagingMediator", "load 5: ${ApiResult.id} ")

                Log.d("PagingMediator", "load 5: ${ApiResult.id} ")
                db.remoteKeysDao().remoteKeysRepoId(ApiResult.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ApiResult>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { ApiResult ->
                db.remoteKeysDao().remoteKeysRepoId(ApiResult.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ApiResult>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { ApiResultID ->
                db.remoteKeysDao().remoteKeysRepoId(ApiResultID)
            }
        }
    }
}
