package com.example.myproj.repository


import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myproj.loadDataFromInternet.GuardianApiService
import com.example.myproj.model.ApiResult
import com.example.myproj.paggingSource.PagingMediator
import com.example.myproj.roomDataBase.NewsDataBase
import com.example.myproj.dataStore.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class GuardianRepository(
    private val dataStore: DataStore<Preferences>,
    private val apiService: GuardianApiService,
    private val db: NewsDataBase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getGuardianData(pageSize: Int , orderBy :String, fromDate : String): Flow<PagingData<ApiResult>> {
        Log.d("GuardianRepository", "getGuardianData: $pageSize $orderBy $fromDate ")
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            remoteMediator = PagingMediator(
                null,
                apiService,
                db,
                orderBy,
                fromDate
            ),
            pagingSourceFactory = {
                db.newsDao().getAllData()
            }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getGuardianDataBySection(section: String, pageSize: Int, orderBy :String, fromDate : String): Flow<PagingData<ApiResult>> {
        Log.d("GuardianRepository", "getGuardianData: $pageSize $orderBy $fromDate ")
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            remoteMediator = PagingMediator(
                section,
                apiService,
                db,
                orderBy,
                fromDate
            ),
            pagingSourceFactory = {
                db.newsDao().getData(section)
            }
        ).flow
    }


    suspend fun saveToDataStore(
        numberOfItem: String? = null,
        orderBy: String?,
        fromDate: String?,
        colorTheme: String?,
        textSize: String?
    ) {
        numberOfItem?.let {
            dataStore.edit { settings ->
                settings[PreferencesKeys.numberOfItem] = numberOfItem
            }
        }
        orderBy?.let {
            dataStore.edit { settings ->
                settings[PreferencesKeys.orderBy] = orderBy
            }
        }
        fromDate?.let {
            dataStore.edit { settings ->
                settings[PreferencesKeys.fromDate] = fromDate
            }
        }
        colorTheme?.let {
            dataStore.edit { settings ->
                settings[PreferencesKeys.colorTHeme] = colorTheme
            }
        }
        textSize?.let {
            dataStore.edit { settings ->
                settings[PreferencesKeys.textSize] = textSize
            }
        }
    }

    val numberOI: Flow<String> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        }
    }.map { preferences ->
        val numberOfItem = preferences[PreferencesKeys.numberOfItem] ?: "10"
        numberOfItem
    }
    val orderB: Flow<String> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        }
    }.map { preferences ->
        val orderBy = preferences[PreferencesKeys.orderBy] ?: "newest"
        orderBy
    }
    val fromD: Flow<String> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        }
    }.map { preferences ->
        val fromDate = preferences[PreferencesKeys.fromDate] ?: "2021-01-01"
        fromDate
    }
    val colorT: Flow<String> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        }
    }.map { preferences ->
        val colorTheme = preferences[PreferencesKeys.colorTHeme] ?: "white"
        colorTheme
    }
    val textS: Flow<String> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        }
    }.map { preferences ->
        val textSize = preferences[PreferencesKeys.textSize] ?: "medium"
        textSize
    }
}