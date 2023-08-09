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
            val response = apiService.getGuardianData(section, page)
            val endOfPageReached = response.body()?.response?.results?.isEmpty() == true
            Log.d("PagingMediator", "PagingMediator: $page $endOfPageReached")
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeysSection(section)
                    db.newsDao().deleteData(section)
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
                db.remoteKeysDao().insertAll(keys)
                db.newsDao().insertAll(response.body()?.response?.results ?: emptyList())
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPageReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ApiResult>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { ApiResult ->
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
