package com.example.myproj.paggingSource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.android.codelabs.paging.db.RemoteKeys
import com.example.myproj.loadDataFromInternet.GuardianApiService
import com.example.myproj.model.ApiResult
import com.example.myproj.roomDataBase.NewsDataBase
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PagingMediator(
    private val section:String?,
    private val apiService: GuardianApiService,
    private val db: NewsDataBase
):RemoteMediator<Int,ApiResult>(){
    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH
    override suspend fun load(loadType: LoadType, state: PagingState<Int, ApiResult>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            val response = apiService.getGuardianData(section, page)
            val result = response.body()?.response?.results
            db.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys()
                    //db.newsDao().deleteAllData()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (response.body()?.response?.results?.isEmpty() == true) null else page + 1
                val keys = result?.map {
                    RemoteKeys(NewsId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                db.remoteKeysDao().insertAll(keys ?: emptyList())
                db.newsDao().insertAll(result ?: emptyList())
            }
            return MediatorResult.Success(endOfPaginationReached = response.body()?.response?.results?.isEmpty() == true)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ApiResult>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { ApiResult ->
                // Get the remote keys of the last item retrieved
                db.remoteKeysDao().remoteKeysRepoId(ApiResult.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ApiResult>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { ApiResult ->
                // Get the remote keys of the first items retrieved
                db.remoteKeysDao().remoteKeysRepoId(ApiResult.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ApiResult>): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { ApiResultID ->
                db.remoteKeysDao().remoteKeysRepoId(ApiResultID)
            }
        }
    }
}
